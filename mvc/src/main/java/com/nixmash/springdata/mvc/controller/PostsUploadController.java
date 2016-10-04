package com.nixmash.springdata.mvc.controller;

import com.nixmash.springdata.jpa.common.ApplicationSettings;
import com.nixmash.springdata.jpa.model.PostImage;
import com.nixmash.springdata.jpa.service.PostService;
import com.nixmash.springdata.jpa.utils.SharedUtils;
import com.nixmash.springdata.mail.service.FmService;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@Controller
@RequestMapping(value = "/posts")
public class PostsUploadController {

    private static final Logger logger = LoggerFactory.getLogger(PostsUploadController.class);

    private static final String POSTS_PLAY_VIEW = "posts/photos";
    private static final String IMAGE_PATTERN = "([^\\s]+(\\.(?i)(jpg|png|gif))$)";

    private Pattern pattern;

    private final PostService postService;
    private final FmService fmService;
    private final ApplicationSettings applicationSettings;

    @Autowired
    public PostsUploadController(PostService postService,
                                 FmService fmService,
                                 ApplicationSettings applicationSettings) {
        this.postService = postService;
        this.fmService = fmService;
        this.applicationSettings = applicationSettings;
    }

    @RequestMapping(value = "/photos", method = GET)
    public String play(Model model) {
        model.addAttribute("parentId", SharedUtils.randomNegativeId());
        model.addAttribute("fileuploading", fmService.getFileUploadingScript());
        model.addAttribute("fileuploaded", fmService.getFileUploadedScript());
        return POSTS_PLAY_VIEW;
    }

    @RequestMapping(value = "/photos/upload/{parentId}", method = GET)
    public @ResponseBody Map list(@PathVariable Long parentId) {
        logger.debug("uploadGet called");
        List<PostImage> list = postService.getPostImages(parentId);
        String urlBase = "/posts/photos";
        for (PostImage image : list) {
            image.setUrl(urlBase + "/picture/" + image.getId());
            image.setThumbnailUrl(urlBase + "/thumbnail/" + image.getId());
            image.setDeleteUrl(urlBase + "/delete/" + image.getId());
            image.setDeleteType("DELETE");
        }
        Map<String, Object> files = new HashMap<>();
        files.put("files", list);
        logger.debug("Returning: {}", files);
        return files;
    }

    @RequestMapping(value = "/photos/upload/{parentId}", method = POST)
    public @ResponseBody Map uploadDto(@PathVariable long parentId,
                                       MultipartHttpServletRequest request) {
        logger.debug("uploadPost called");
        Iterator<String> itr = request.getFileNames();
        MultipartFile mpf;
        List<PostImage> list = new LinkedList<>();
        pattern = Pattern.compile(IMAGE_PATTERN);
        String fileStoragePath = getFileStoragePath(parentId);

        while (itr.hasNext()) {
            mpf = request.getFile(itr.next());
            String filename = mpf.getOriginalFilename();
            logger.info("Uploading {}", filename);

            String newFilenameBase = UUID.randomUUID().toString();
            String fileExtension = FilenameUtils.getExtension(filename).toLowerCase();

            // demo mode files over 1MB are rejected
            if (parentId < 0 && mpf.getSize() > 1048576)
                break;

            // only png/gif/jpg files accepted
            if (!isValidImageFile(filename))
                break;

            String newFilename = String.format("%s.%s", newFilenameBase, fileExtension);
            String contentType = mpf.getContentType();

            File temporaryFile = new File(newFilename);
            File storageFile = new File(fileStoragePath + newFilename);
            try {

                mpf.transferTo(temporaryFile);
                FileUtils.copyFile(new File("/tmp/" + temporaryFile), storageFile);

                BufferedImage thumbnail = Thumbnails.of(FileUtils.getFile(storageFile))
                        .size(160, 160)
                        .allowOverwrite(true)
                        .outputFormat("png")
                        .asBufferedImage();
                String thumbnailFilename = newFilenameBase + "-thumbnail.png";
                File thumbnailFile = new File(fileStoragePath + thumbnailFilename);
                ImageIO.write(thumbnail, "png", thumbnailFile);

                PostImage image = new PostImage();
                image.setPostId(parentId);
                image.setName(mpf.getOriginalFilename());
                image.setThumbnailFilename(thumbnailFilename);
                image.setNewFilename(newFilename);
                image.setContentType(contentType);
                image.setSize(mpf.getSize());
                image.setThumbnailSize(thumbnailFile.length());
                image = postService.addImage(image);

                image.setUrl("/posts/photos/picture/" + image.getId());
                image.setThumbnailUrl("/posts/photos/thumbnail/" + image.getId());
                image.setDeleteUrl("/posts/photos/delete/" + image.getId());
                image.setDeleteType("DELETE");

                list.add(image);

            } catch (IOException e) {
                logger.error("Could not upload file " + mpf.getOriginalFilename(), e);
            }

        }

        Map<String, Object> files = new HashMap<>();
        files.put("files", list);
        return files;
    }

    @RequestMapping(value = "/photos/picture/{id}", method = GET)
    public void picture(HttpServletResponse response, @PathVariable Long id) {
        PostImage image = postService.getPostImage(id);
        String fileStoragePath = getFileStoragePath(image.getPostId());
        File imageFile = new File(fileStoragePath + image.getNewFilename());
        response.setContentType(image.getContentType());
        response.setContentLength(image.getSize().intValue());
        try {
            InputStream is = new FileInputStream(imageFile);
            IOUtils.copy(is, response.getOutputStream());
        } catch (IOException e) {
            logger.error("Could not show picture " + id, e);
        }
    }

    @RequestMapping(value = "/photos/thumbnail/{id}", method = GET)
    public void thumbnail(HttpServletResponse response, @PathVariable Long id) {
        PostImage image = postService.getPostImage(id);
        String fileStoragePath = getFileStoragePath(image.getPostId());
        File imageFile = new File(fileStoragePath + image.getThumbnailFilename());
        response.setContentType(image.getContentType());
        response.setContentLength(image.getThumbnailSize().intValue());
        try {
            InputStream is = new FileInputStream(imageFile);
            IOUtils.copy(is, response.getOutputStream());
        } catch (IOException e) {
            logger.error("Could not show thumbnail " + id, e);
        }
    }

    @RequestMapping(value = "/photos/delete/{id}", method = DELETE)
    public @ResponseBody List delete(@PathVariable Long id) {
        PostImage image = postService.getPostImage(id);
        String fileStoragePath = getFileStoragePath(image.getPostId());
        File imageFile = new File(fileStoragePath + image.getNewFilename());
        imageFile.delete();
        File thumbnailFile = new File(fileStoragePath + image.getThumbnailFilename());
        thumbnailFile.delete();
        postService.deleteImage(image);
        List<Map<String, Object>> results = new ArrayList<>();
        Map<String, Object> success = new HashMap<>();
        success.put("success", true);
        results.add(success);
        return results;
    }

    private boolean isValidImageFile(final String image) {
        Matcher matcher = pattern.matcher(image);
        return matcher.matches();
    }

    private String getFileStoragePath(long parentId) {
        String fileStoragePath = applicationSettings.getPostImagePath();
        if (parentId < 0) {
            fileStoragePath = applicationSettings.getPostDemoImagePath();
        }
        return fileStoragePath;
    }
}
