<#-- @ftlvariable name="post" type="com.nixmash.springdata.jpa.model.Post" -->
<div class="post link-summary">
<#if  post.postImage??>
    <img alt="" src="${post.postImage}" class="thumbnail-image"/>
</#if>
    <h3><a target="_blank" href="/posts/post/${post.postName}">${post.postTitle}</a></h3>
    <div class="post-content">${post.postContent}</div>
    <div class="post-footer">
    <#include "includes/footer.ftl">
    </div>

</div>
