<html>
<body>
<div>
   <#include "includes/header.ftl">
</div>
<h3>${greeting}</h3>
<p>
   Use the link below to verify your email address and complete your User Account setup. Thank you and welcome to NixMash Spring!
</p>
<p><strong><a href="${verifyLink}">Verify your Email Address</a></strong></p>
<p style="margin-top:60px; font-size=smaller;font-style: italic;">
    A message from ${memberServices}
</p>
    <#include "includes/footer.ftl">
</body>
</html>

