package info.mastera.userserviceapi.specification;

import info.mastera.security.dto.AccountDto;
import info.mastera.security.utils.AuthUtils;
import info.mastera.userserviceapi.dto.TripSearchRequest;
import info.mastera.userserviceapi.exception.UnauthorizedException;
import info.mastera.userserviceapi.model.Place;
import info.mastera.userserviceapi.model.Trip;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

public class TripSpecification {

    private TripSpecification() {
    }

    public static Specification<Trip> filter(TripSearchRequest searchRequest) {
        Long accountId = Optional.ofNullable(AuthUtils.getAccount())
                .map(AccountDto::getId)
                .orElseThrow(() -> new UnauthorizedException("You are not logged int of user ID was not found in system"));

        Specification<Trip> specification = Specification.where((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("ownerId"), accountId));

        if (searchRequest.from() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("date"), searchRequest.from()));
        }
        if (searchRequest.to() != null) {
            specification = specification.and((root, query, cb) ->
                    cb.lessThanOrEqualTo(root.get("date"), searchRequest.to()));
        }
        if (searchRequest.country() != null && !searchRequest.country().isEmpty()) {
            specification = specification.and((root, query, cb) -> {
                Join<Trip, Place> placeJoin = root.join("place");
                return cb.equal(cb.upper(placeJoin.get("country")), searchRequest.country().toUpperCase());
            });
        }
        if (searchRequest.companion() != null && !searchRequest.companion().isEmpty()) {
            specification = specification.and(containsCompanion(searchRequest.companion()));
        }
        return specification;
    }


    public static Specification<Trip> containsCompanion(String companion) {
        return (root, query, cb) -> {
            Expression<Boolean> expression = cb.function(
                    "jsonb_exists",
                    Boolean.class,
                    root.get("companions"),
                    cb.literal(companion)
            );
            //in case we want to search case-insensitive then we have to use "$.companions[*] ? (@ ilike_regex '.*" + companion + ".*')"
            // but it contains '?' (question mark) what leads to variable in hibernate request
            // This means that we can't use Specification but native query only
            return cb.isTrue(expression);
        };
    }

    @SuppressWarnings("unused")
    public static Specification<Trip> containsCompanions(Collection<String> companions) {
        return (root, query, cb) -> {
            String companionsText = companions.stream()
                    .map(companion -> "\"" + companion + "\"") // Add quotes around each element
                    .collect(Collectors.joining(",", "{", "}"));
            Expression<Boolean> expression = cb.function("jsonb_exists_any", Boolean.class, root.get("companions"), cb.literal(companionsText));
            return cb.isTrue(expression);
        };
    }
}
