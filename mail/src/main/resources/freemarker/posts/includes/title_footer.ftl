<#-- @ftlvariable name="post" type="com.nixmash.springdata.jpa.model.Post" -->
<div class="post-tags">
<#list post.tags as tag>
    <#assign url  = tag.tagValue?lower_case>
    <span class="taglink">
              <a href="/posts/tag/${url}" class="big label label-default">${tag.tagValue}</a>
          </span>
</#list>
</div>
<div class="post-date"><span>Posted on </span><abbr title="${post.postDate}">${postCreated}</abbr>
</div>