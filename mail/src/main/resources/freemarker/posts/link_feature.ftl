<div class="post link-feature">
    <h3><a target="_blank" href="/posts/post/${post.postName}">${post.postTitle}</a></h3>
    <div class="post-content">${post.postContent}</div>
<#if  post.postImage??>
    <img alt="" src="${post.postImage}" class="feature-image"/>
</#if>
    <div class="post-footer">
    <#include "includes/footer.ftl">
    </div>

</div>
