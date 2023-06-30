package com.test.common.utils;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.common.result.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @return
 */
public class ResponseUtil {
    public static void out(HttpServletResponse response, Result r){
        ObjectMapper mapper= new ObjectMapper();
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        try{
            mapper.writeValue(response.getWriter(),r);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonGenerationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
