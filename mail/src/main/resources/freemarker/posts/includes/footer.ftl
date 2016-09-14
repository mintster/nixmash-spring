<#-- @ftlvariable name="post" type="com.nixmash.springdata.jpa.model.Post" -->
<div class="post-date"><span>Posted on </span><abbr title="${post.postDate}">${postCreated}</abbr>
</div>
<div class="post-tags">
<#list post.tags as tag>
    <#assign url  = tag.tagValue?lower_case>
    <span class="taglink">
              <a href="/posts/tag/${url}" class="big label label-default">${tag.tagValue}</a>
          </span>
</#list>
</div>
<div class="share-like-row">
    <div class="share-links">
        <a class="share share_email"
           href="mailto:?subject=${post.postTitle}&amp;body=${shareUrl}">
            <img src="/images/share/email.png" title="Email" class="share-img-sm" alt="Email this to someone"/></a>
        <a class="share share-twitter"
           href="http://twitter.com/share?url=${shareUrl}/&amp;text=${post.postTitle}+via+%40${shareSiteName}"
           target="_blank">
            <img src="/images/share/twitter.png" title="Twitter" class="share-img-sm"
                 alt="Tweet about this on Twitter"/></a>
        <a class="share share-linkedin"
           href="http://www.linkedin.com/shareArticle?mini=true&amp;url=${shareUrl}"
           target="_blank">
            <img src="/images/share/linkedin.png" title="LinkedIn" class="share-img-sm" alt="Share on LinkedIn"/></a>
        <a class="share share-google"
           href="https://plus.google.com/share?url=${shareUrl}"
           target="_blank">
            <img src="/images/share/google.png" title="Google+" class="share-img-sm" alt="Share on Google+"/></a>
        <a class="share share-facebook"
           href="http://www.facebook.com/sharer.php?u=${shareUrl}"
           target="_blank">
            <img src="/images/share/facebook.png" title="Facebook" class="share-img-sm" alt="Share on Facebook"/></a>
    </div>
    <div class="like-button">
        <i class="fa fa-thumbs-up"></i>
        <button class="btn-like" onclick="likePost(${post.postId});">Like</button>
        <span class="like-count" id="output-${post.postId}">${post.likesCount}</span>
    </div>
</div>

<#if  post.postType.name() == "LINK">
<a target="_blank" href="${post.postLink}" class="remote-link" title="Go to External Site">${post.postSource}</a>
</#if>

