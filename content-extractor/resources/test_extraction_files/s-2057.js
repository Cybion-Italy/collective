function na_getKwds () {
  var meta = document.all ?
    document.all.tags('META') :
    document.getElementsByTagName ?
    document.getElementsByTagName ('META') : new Array();

  var kwds = "";
  for (var m = 0; m < meta.length; m++)
    if (meta[m].name == 'keywords')
      if(kwds != "")
        kwds = kwds + "," + meta[m].content;
      else
        kwds = meta[m].content;

  return kwds;
}

na_random = new String (Math.random()); na_random = na_random.substring(2,11);
na_url = location.href;
na_referrer = document.referrer;
na_title = document.title;
na_frame_url  = location.protocol + "//f.nexac.com/e/a-1148/s-2057.xgi?na_random=" + na_random;
na_frame_url += "&na_url=" + escape(na_url);
na_frame_url += "&na_referrer=" + escape(na_referrer);
na_frame_url += "&na_title=" + escape(na_title);
na_frame_url += "&na_bksite=" + "1381";
na_frame_url += "&na_imsite=" + "5414";
na_frame_url += "&na_iitaxid=" + "";
na_frame_url += "&na_iicatid=" + "";
na_frame_url += "&na_trncnv=" + "I7GDh6-0CwLBknJI751TDv9h-esih_oRSP4sQ6ozt1Nh968tNVqFx7ajiOqLyRv77WWiu5P_OLAQgCYbvjjePg";
na_frame_url += "&na_trntrg=" + "Qp8nfDw8KEoimj414RcMRZDFyrFNhUlJBL6teQR0JRZh968tNVqFx7ajiOqLyRv7j1WtKzCKHfNpyA-Jo1t12Q";
na_frame_url += "&na_trncrt=" + "";
var w = window;
if (w.na_ci) {
        na_frame_url += "&na_ci="     + escape(na_ci);
}
if (w.na_oi) {
        na_frame_url += "&na_oi="     + escape(na_oi);
}
if (w.na_ov) {
        na_frame_url += "&na_ov="     + escape(na_ov);
}
if (w.na_sc) {
        na_frame_url += "&na_sc="     + escape(na_sc);
}
if (w.na_pc) {
        na_frame_url += "&na_pc="     + escape(na_pc);
}
if (w.na_nb) {
        na_frame_url += "&na_nb="     + escape(na_nb);
}
if (w.na_fn) {
        na_frame_url += "&na_fn="     + escape(na_fn);
}
if (w.na_ln) {
        na_frame_url += "&na_ln="     + escape(na_ln);
}
if (w.na_zc) {
        na_frame_url += "&na_zc="     + escape(na_zc);
}
if (w.na_cy) {
        na_frame_url += "&na_cy="     + escape(na_cy);
}
if (w.na_st) {
        na_frame_url += "&na_st="     + escape(na_st);
}
if (w.na_a1) {
        na_frame_url += "&na_a1="     + escape(na_a1);
}
if (w.na_a2) {
        na_frame_url += "&na_a2="     + escape(na_a2);
}
if (w.na_em) {
        na_frame_url += "&na_em="     + escape(na_em);
}
if (w.na_ev) {
        na_frame_url += "&na_ev="     + escape(na_ev);
}
if (w.na_qr) {
        na_frame_url += "&na_qr="     + escape(na_qr);
}
if (w.na_ct) {
        na_frame_url += "&na_ct="     + escape(na_ct);
}
na_frame_url += "&na_kw=" + escape(na_getKwds());


document.write("<div id=\"nexacdiv\"><iframe src=\"" + na_frame_url + "\" name=\"naframe1\" height=\"0\" width=\"0\" frameborder=\"0\"></iframe></div>");

sensitive = "N";
na_timeout = 2000;
if (sensitive != "Y") {
	na_timeout = 6000;
}
 
setTimeout('timeOutTag()',na_timeout);

function timeOutTag() {
        document.getElementById("nexacdiv").innerHTML = '';
}

