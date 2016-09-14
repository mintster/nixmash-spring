<#-- @ftlvariable name="post" type="com.nixmash.springdata.jpa.model.Post" -->
<div class="post note">
    <h3><a target="_blank"
           href="/posts/post/${post.postName}">${post.postTitle}</a></h3>
<#if  post.singleImage??>
    <div class="post-single-photo">
        <img src="${post.singleImage.url}${post.singleImage.newFilename}" alt="" class="post-photo-single" />
    </div>
</#if>
    <div class="post-content">${post.postContent}</div>
    <div class="post-footer">
    <#include "includes/footer.ftl">
    </div>
</div>
