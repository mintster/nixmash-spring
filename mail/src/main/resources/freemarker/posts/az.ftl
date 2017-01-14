<#-- @ftlvariable name="alphaPost" type="com.nixmash.springdata.jpa.dto.PostDTO" -->
<#-- @ftlvariable name="alphaLink" type="com.nixmash.springdata.jpa.dto.AlphabetDTO" -->
<div id="grid-table">
    <div class="col-lg-9 col-centered">
        <div class="container">
            <div class="btn-alphalinks">
                <div class="btn-group btn-group-sm">
                <#list alphaLinks as alphaLink>
                    <a href="#${alphaLink.alphaCharacter}"
                       class="btn btn-default"${alphaLink.active?then('',' disabled=\"disabled\"')}>${alphaLink.alphaCharacter}</a>
                </#list>
                </div>
            </div>
        </div>
        <div class="container">
            <div class="alphaposts">
            <#list alphaLinks as alphaLink>
                <#assign key = alphaLink.alphaCharacter?replace("-", "")>
                <#if alphaLink.active>
                    <h2 class="alphaheading"><a name="${alphaLink.alphaCharacter}">${alphaLink.alphaCharacter}</a>
                    </h2>
                    <div class="alphasection">
                        <#list alphaPosts as alphaPost>
                            <#if alphaPost.alphaKey == key>
                                <p>
                                    <a href="/posts/post/${alphaPost.postName}">${alphaPost.postTitle}</a>
                                </p>
                            </#if>
                        </#list>
                        <i class="fa fa-caret-square-o-up" aria-hidden="true"></i>
                        <a href="#top" class="alpha-back-to-top">${backToTop}</a>
                    </div>
                </#if>
            </#list>
            </div>
        </div>
    </div>
</div>
