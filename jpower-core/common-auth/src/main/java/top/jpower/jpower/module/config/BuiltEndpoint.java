package top.jpower.jpower.module.config;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author mr.g
 * @date 2022/10/5 17:08
 */
@RestController
@RequestMapping
public class BuiltEndpoint {

    public static final String PATH = "/getAllFunction";


    @GetMapping(value = PATH)
    public Map<String,List<Map<String,Object>>> getAllFunction(){
        return FunctionGenerate.functions;
    }

}
