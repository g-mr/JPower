package top.jpower.core.swagger.utils;

import org.springframework.util.AntPathMatcher;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.PathSelectors;
import top.jpower.core.swagger.annotation.ApiGroup;
import top.jpower.core.util.utils.Fc;

import java.util.List;
import java.util.function.Predicate;

/**
 * @author mr.g
 * @date 2024-9-5 21:57
 * @description
 */
public class PathMatch {

    private PathMatch(){
        throw new UnsupportedOperationException();
    }

    /**
     * Predicate that evaluates the supplied ant pattern
     *
     * @author mr.g
     * @param antPatterns - ant Pattern
     * @return predicate that matches a particular ant pattern
     */
    public static Predicate<String> ant(final List<String> antPatterns) {

        if (Fc.isEmpty(antPatterns)){
            return PathSelectors.none();
        }
        Predicate<String> predicates = PathSelectors.none();

        for (String antPattern : antPatterns) {
            // if (negate){
            //     predicates = predicates.and((input -> new AntPathMatcher().match(antPattern, input))).negate();
            // } else {
                predicates = predicates.or((input -> new AntPathMatcher().match(antPattern, input)));
            // }
        }

        return predicates;
    }

    public static Predicate<RequestHandler> withGroupName(final String groupName) {
        return input -> input.findControllerAnnotation(ApiGroup.class).map(apiGroup -> Fc.contains(apiGroup.value(), groupName)).orElse(false);
    }

}
