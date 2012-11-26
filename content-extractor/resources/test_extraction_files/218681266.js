/* Vermont-12.4.0-1012 */
rsinetsegs=['J08782_10018','J08782_10025','J08782_10026'];
var rsiExp=new Date((new Date()).getTime()+2419200000);
var rsiDom=location.hostname;
rsiDom=rsiDom.replace(/.*(\.[\w\-]+\.[a-zA-Z]{3}$)/,'$1');
rsiDom=rsiDom.replace(/.*(\.[\w\-]+\.\w+\.[a-zA-Z]{2}$)/,'$1');
rsiDom=rsiDom.replace(/.*(\.[\w\-]{3,}\.[a-zA-Z]{2}$)/,'$1');
var rsiSegs="";
var rsiPat=/.*_5.*/;
for(x=0;x<rsinetsegs.length;++x){if(!rsiPat.test(rsinetsegs[x]))rsiSegs+='|'+rsinetsegs[x];}
document.cookie="rsi_segs="+(rsiSegs.length>0?rsiSegs.substr(1):"")+";expires="+rsiExp.toGMTString()+";path=/;domain="+rsiDom;
if(typeof(DM_onSegsAvailable)=="function"){DM_onSegsAvailable(['J08782_10018','J08782_10025','J08782_10026'],'j08782');}