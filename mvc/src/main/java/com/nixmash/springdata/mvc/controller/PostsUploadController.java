package com.nixmash.springdata.mvc.controller;

import com.nixmash.springdata.jpa.model.Image;
import com.nixmash.springdata.jpa.service.PostService;
import com.nixmash.springdata.mail.service.TemplateService;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@RequestMapping(value = "/posts")
public class PostsUploadController {

    private static final Logger logger = LoggerFactory.getLogger(PostsUploadController.class);

    private static final String POSTS_PLAY_VIEW = "posts/play";

    private final PostService postService;
    private final TemplateService templateService;

    @Value("${file.upload.directory}")
    private String fileUploadDirectory;

    @Autowired
    public PostsUploadController(PostService postService, TemplateService templateService) {
        this.postService = postService;
        this.templateService = templateService;
    }

    @RequestMapping(value = "/play", method = GET)
    public String play(Model model) {
//        FileUploadDTO fileUploadDTO = new FileUploadDTO();
//        model.addAttribute("fileUploadDTO", fileUploadDTO);
        model.addAttribute("fileuploading", templateService.getFileUploadingScript());
        model.addAttribute("fileuploaded", templateService.getFileUploadedScript());
        return POSTS_PLAY_VIEW;
    }


    @RequestMapping(value = "/play/upload", method = RequestMethod.GET)
    public @ResponseBody
    Map list() {
        logger.debug("uploadGet called");
        List<Image> list = postService.getAllPostImages();
        for(Image image : list) {
            image.setUrl("/posts/play/picture/"+image.getId());
            image.setThumbnailUrl("/posts/play/thumbnail/"+image.getId());
            image.setDeleteUrl("/posts/play/delete/"+image.getId());
            image.setDeleteType("DELETE");
        }
        Map<String, Object> files = new HashMap<>();
        files.put("files", list);
        logger.debug("Returning: {}", files);
        return files;
    }

    @RequestMapping(value = "/play/upload", method = RequestMethod.POST)
    public @ResponseBody Map upload(MultipartHttpServletRequest request, HttpServletResponse response) {
        logger.debug("uploadPost called");
        Iterator<String> itr = request.getFileNames();
        MultipartFile mpf;
        List<Image> list = new LinkedList<>();

        while (itr.hasNext()) {
            mpf = request.getFile(itr.next());
            logger.debug("Uploading {}", mpf.getOriginalFilename());

            String newFilenameBase = UUID.randomUUID().toString();
            String originalFileExtension = mpf.getOriginalFilename().substring(mpf.getOriginalFilename().lastIndexOf("."));
            String newFilename = newFilenameBase + originalFileExtension;
            String storageDirectory = fileUploadDirectory;
            String contentType = mpf.getContentType();

            File temporaryFile = new File(newFilename);
            File storageFile = new File(storageDirectory + newFilename);
            try {
                mpf.transferTo(temporaryFile);
                FileUtils.copyFile(new File("/tmp/" + temporaryFile), storageFile);

                BufferedImage thumbnail =  Thumbnails.of(FileUtils.getFile(storageFile))
                        .forceSize(290, 290)
                        .allowOverwrite(true)
                        .outputFormat("png")
                        .asBufferedImage();
                String thumbnailFilename = newFilenameBase + "-thumbnail.png";
                File thumbnailFile = new File(storageDirectory + "/" + thumbnailFilename);
                ImageIO.write(thumbnail, "png", thumbnailFile);

                Image image = new Image();
                image.setName(mpf.getOriginalFilename());
                image.setThumbnailFilename(thumbnailFilename);
                image.setNewFilename(newFilename);
                image.setContentType(contentType);
                image.setSize(mpf.getSize());
                image.setThumbnailSize(thumbnailFile.length());
                image = postService.addImage(image);

                image.setUrl("/posts/play/picture/"+image.getId());
                image.setThumbnailUrl("/posts/play/thumbnail/"+image.getId());
                image.setDeleteUrl("/posts/play/delete/"+image.getId());
                image.setDeleteType("DELETE");

                list.add(image);

            } catch(IOException e) {
                logger.error("Could not upload file "+mpf.getOriginalFilename(), e);
            }

        }

        Map<String, Object> files = new HashMap<>();
        files.put("files", list);
        return files;
    }

    @RequestMapping(value = "/play/picture/{id}", method = RequestMethod.GET)
    public void picture(HttpServletResponse response, @PathVariable Long id) {
        Image image = postService.getPostImage(id);
        File imageFile = new File(fileUploadDirectory+"/"+image.getNewFilename());
        response.setContentType(image.getContentType());
        response.setContentLength(image.getSize().intValue());
        try {
            InputStream is = new FileInputStream(imageFile);
            IOUtils.copy(is, response.getOutputStream());
        } catch(IOException e) {
            logger.error("Could not show picture "+id, e);
        }
    }

    @RequestMapping(value = "/play/thumbnail/{id}", method = RequestMethod.GET)
    public void thumbnail(HttpServletResponse response, @PathVariable Long id) {
        Image image =postService.getPostImage(id);
        File imageFile = new File(fileUploadDirectory+"/"+image.getThumbnailFilename());
        response.setContentType(image.getContentType());
        response.setContentLength(image.getThumbnailSize().intValue());
        try {
            InputStream is = new FileInputStream(imageFile);
            IOUtils.copy(is, response.getOutputStream());
        } catch(IOException e) {
            logger.error("Could not show thumbnail "+id, e);
        }
    }

    @RequestMapping(value = "/play/delete/{id}", method = RequestMethod.DELETE)
    public @ResponseBody List delete(@PathVariable Long id) {
        Image image = postService.getPostImage(id);
        File imageFile = new File(fileUploadDirectory+"/"+image.getNewFilename());
        imageFile.delete();
        File thumbnailFile = new File(fileUploadDirectory+"/"+image.getThumbnailFilename());
        thumbnailFile.delete();
        postService.deleteImage(image);
        List<Map<String, Object>> results = new ArrayList<>();
        Map<String, Object> success = new HashMap<>();
        success.put("success", true);
        results.add(success);
        return results;
    }
}
