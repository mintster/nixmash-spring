package com.nixmash.springdata.mvc.components;

import com.nixmash.springdata.jpa.common.ApplicationSettings;
import com.nixmash.springdata.jpa.model.Post;
import com.nixmash.springdata.jpa.service.PostService;
import com.rometools.rome.feed.rss.Channel;
import com.rometools.rome.feed.rss.Content;
import com.rometools.rome.feed.rss.Description;
import com.rometools.rome.feed.rss.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.feed.AbstractRssFeedView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by daveburke on 7/13/16.
 */
@Component("rssPostFeedView")
public final class RssPostFeedView extends AbstractRssFeedView {

    private static final int NUMBER_OF_ITEMS = 10;

    private PostService postService;
    private ApplicationSettings applicationSettings;

    @Autowired
    public RssPostFeedView(PostService postService,
                           ApplicationSettings applicationSettings) {
        this.postService = postService;
        this.applicationSettings = applicationSettings;
    }

    @Override
    protected Channel newFeed() {
        Channel channel = new Channel("rss_2.0");
        channel.setLink(applicationSettings.getBaseUrl() + "/posts/feed/");
        channel.setTitle(applicationSettings.getRssChannelTitle());
        channel.setDescription(applicationSettings.getRssChannelDescription());
        postService.getOneMostRecent()
                .ifPresent(p -> channel.setPubDate(Date.from(p.getPostDate().toInstant())));
        return channel;
    }

    @Override
    protected List<Item> buildFeedItems(Map<String, Object> model,
                                        HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse) throws Exception {
        return postService.getPublishedPosts(0, NUMBER_OF_ITEMS).getContent().stream()
                .map(this::createItem)
                .collect(Collectors.toList());
    }

    private Item createItem(Post post) {
        Item item = new Item();
        item.setLink(applicationSettings.getBaseUrl() + "/posts/post/" + post.getPostName());
        item.setTitle(post.getPostTitle());
        item.setDescription(createDescription(post));
        item.setPubDate(getPostDate(post));
        return item;
    }

    private Description createDescription(Post post) {
        Description description = new Description();
        description.setType(Content.HTML);
        description.setValue(post.getPostContent());
        return description;
    }

    private Date getPostDate(Post post) {
        return Date.from(post.getPostDate().toInstant());
    }
}