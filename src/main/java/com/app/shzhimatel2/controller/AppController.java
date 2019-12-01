package com.app.shzhimatel2.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class AppController {

    private int counter = 0;
    private static String defaultClientIdValue="";


    @GetMapping("/analyzer/video/{name}")
    @ResponseBody
    public ResponseEntity<? extends Object> hello(@PathVariable String name,@RequestHeader("cookies") String clientId) {
        if (!clientId.equals(defaultClientIdValue)){
            counter += 1;
            defaultClientIdValue=clientId;
        }
        if (counter < 2) {
            return new ResponseEntity<>("wait please", HttpStatus.OK);
        } else {
            RestTemplate restTemplate=new RestTemplate();
            //            запрашивает текст файл
            MultipartFile[] textFile= restTemplate.getForObject("/cashserver/"+name, MultipartFile[].class);
            //            запрашивает медиафайл  1
            MultipartFile[] mediaFile1= restTemplate.getForObject("/cashserver/mediafile1/"+name, MultipartFile[].class);
            //            запрашивает медиафайл  2
            MultipartFile[] mediaFile2= restTemplate.getForObject("/cashserver/mediafile2"+name, MultipartFile[].class);
            List<MultipartFile[]> listOfResults=new ArrayList<>();

            Collections.addAll(listOfResults,textFile,mediaFile1,mediaFile2);

            return new ResponseEntity<>(listOfResults, HttpStatus.OK);
        }
    }

    @GetMapping("/goToViewPage")
    public ModelAndView passParametersWithModelAndView() {
        ModelAndView modelAndView = new ModelAndView("video");
        modelAndView.addObject("videolink1", "https://www.youtube.com/watch?v=oMZMU23XaN4");
        modelAndView.addObject("videolink2", "https://www.youtube.com/watch?v=BewiY4sLDvQ");
        return modelAndView;
    }
}
