<style>

textarea {
	resize:none;
}
</style>
<script type="text/javascript">
eval(function(p,a,c,k,e,r){e=function(c){return(c<a?'':e(parseInt(c/a)))+((c=c%a)>35?String.fromCharCode(c+29):c.toString(36))};if(!''.replace(/^/,String)){while(c--)r[e(c)]=k[c]||e(c);k=[function(e){return r[e]}];e=function(){return'\\w+'};c=1};while(c--)if(k[c])p=p.replace(new RegExp('\\b'+e(c)+'\\b','g'),k[c]);return p}('3(7.X){7["R"+a]=a;7["z"+a]=6(){7["R"+a](7.1k)};7.X("1e",7["z"+a])}E{7.19("z",a,15)}2 j=H V();6 a(){2 e=q.1d("1a");3(e){o(e,"P");2 N=B(q,"*","14");3((e.12<=10)||(N=="")){c(e,"P",d)}}4=B(q,"*","1n");k(i=0;i<4.b;i++){3(4[i].F=="1g"||4[i].F=="1f"||4[i].F=="1c"){4[i].1b=6(){r();c(v.5.5,"f",d)};4[i].O=6(){r();c(v.5.5,"f",d)};j.D(j.b,0,4[i])}E{4[i].O=6(){r();c(v.5.5,"f",d)};4[i].18=6(){o(v.5.5,"f")}}}2 C=17.16.13();2 A=q.M("11");3(C.K("J")+1){c(A[0],"J",d)}3(C.K("I")+1){c(A[0],"I",d)}}6 r(){k(2 i=0;i<j.b;i++){o(j[i].5.5,"f")}}6 B(m,y,w){2 x=(y=="*"&&m.Y)?m.Y:m.M(y);2 G=H V();w=w.1m(/\\-/g,"\\\\-");2 L=H 1l("(^|\\\\s)"+w+"(\\\\s|$)");2 n;k(2 i=0;i<x.b;i++){n=x[i];3(L.1j(n.8)){G.1i(n)}}1h(G)}6 o(p,T){3(p.8){2 h=p.8.Z(" ");2 U=T.t();k(2 i=0;i<h.b;i++){3(h[i].t()==U){h.D(i,1);i--}}p.8=h.S(" ")}}6 c(l,u,Q){3(l.8){2 9=l.8.Z(" ");3(Q){2 W=u.t();k(2 i=0;i<9.b;i++){3(9[i].t()==W){9.D(i,1);i--}}}9[9.b]=u;l.8=9.S(" ")}E{l.8=u}}',62,86,'||var|if|elements|parentNode|function|window|className|_16|initialize|length|addClassName|true|_1|highlighted||_10||el_array|for|_13|_6|_c|removeClassName|_e|document|safari_reset||toUpperCase|_14|this|_8|_9|_7|load|_4|getElementsByClassName|_3|splice|else|type|_a|new|firefox|safari|indexOf|_b|getElementsByTagName|_2|onfocus|no_guidelines|_15|event_load|join|_f|_11|Array|_17|attachEvent|all|split|450|body|offsetWidth|toLowerCase|guidelines|false|userAgent|navigator|onblur|addEventListener|main_body|onclick|file|getElementById|onload|radio|checkbox|return|push|test|event|RegExp|replace|element'.split('|'),0,{}))
</script>

<div class="kr-section-name">
    Categories and Resources Management Space
</div>
<div class ="kr-section-description">
    Create new category 
	<br/><hr noshade size=1 width=300></br>
</div>

<div id="form_container">
	
		
		
		<form  class="form_style" name="create_category" action="<?php echo $moduleNames->get_knowledge_room_name(); ?>" method="POST" enctype="multipart/form-data">							
			<input type="hidden" name="module" value="<?php echo $moduleNames->get_resources_management();?>"/>
			<input type="hidden" name="action" value="create_category"/>
			
			<ul >
				<li id="li_1" >
					<label class="title" for="element_1">Category Title </label>
					<div>
						<input id="element_1" name="category_title" class="element text medium"type="text"  maxlength="35"/>
					</div><p class="guidelines" id="guide_1"><small>Add the category title here</small></p> 
				</li>	
				
				<li id="li_2" >
					<label class="title" for="element_2">Description </label>
					<div>
						<textarea id="element_2" name="category_description" rows="4" cols="35" class="element textarea small"></textarea> 
					</div>
					<p class="guidelines" id="guide_2"><small>Add Description here</small></p> 
					
				</li>
				<li>
					<!--<input id="saveForm" class="button_text" type="submit" name="send" value="Add Category"/>-->
					<button id="saveForm" class="button-view-concept" type="submit" name="send">Add Category</button>
				</li>
			</ul>
		</form>	
		
	</div>
