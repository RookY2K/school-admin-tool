<aside id="emailforhelp" class="modal">
    <div>
    	<p><strong>Send email to administrator for additional help.</strong></p>
    	<form action="/emailadmin" method="post">
    	<input type="hidden" name="returnURI" value="<%=request.getRequestURI() %>" />
    	<input type="text" name="targetEmail" value="" placeholder="Your Email" required /><br /><br />
    	<textarea name="message" rows="6" cols="40"  placeholder="Your Message" required ></textarea><br /><br />
    	<input type="submit" class="submit" name="submitEmail" value="Send Email" />
    	</form>
		<a href="#close" title="Close" class="unselectable">Close</a>
    </div>
</aside>
<aside id="emailsent" class="modal">
    <div>
    	<p><strong>Send email to administrator for additional help.</strong></p>

		<p><span class="good-message">Your email has been sent to administrator.</span></p>
		<a href="#close" title="Close" class="unselectable">Close</a>
    </div>
</aside>
<aside id="emailtoomany" class="modal">
    <div>
    	<p><strong>Send email to administrator for additional help.</strong></p>

		<p><span class="error-message">You have sent to many email today.</span></p>
		<a href="#close" title="Close" class="unselectable">Close</a>
    </div>
</aside>