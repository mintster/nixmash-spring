<div class="post nixmash-post">
<#if  post.postImage??>
    <img alt="" src="${post.postImage}" class="thumbnail-image"/>
</#if>
    <h3><a target="_blank" href="/posts/post/${post.postName}">${post.postTitle}</a></h3>
    <div class="post-content">${post.postContent}</div>
    <div class="post-footer">
    <#include "includes/footer.ftl">
    </div>
    <div class="nixmash-tag"><a href="http://nixmash.com" target="_blank">
        <img src="/images/posts/nixmashtag.png" alt=""/></a></div>
</div>
