package com.sneakyxpress.webapp.client.facebook;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.ScriptElement;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.HTML;

/*
 * Code modified from https://code.google.com/p/gwt-examples/wiki/demogwtshare
 */
public class Share extends Composite {
  private HTML hFacebook;
  private String url;

  public Share(String t) {
    url = t;
	  
    VerticalPanel vpMain = new VerticalPanel();
    initWidget(vpMain);
    
    HorizontalPanel vpShare = new HorizontalPanel();
    vpMain.add(vpShare);
    
    hFacebook = new HTML("&nbsp;", true);
    vpShare.add(hFacebook);
    
    setup();
  }

  private void setup() {
    
    setupFacebookScript();
    
    drawFacebookButton();
  }
  
  private void drawFacebookButton() {
    String s = "<fb:like " +
    "href=\"" + url + "\" " +
    "layout=\"standard\" " +
    "show_faces=\"true\" " +
    "share=\"true\" " +
    "</fb:like>";
    
    getHFacebook().setHTML(s);
  }
  
  private void setupFacebookScript() {
    Document doc = Document.get();
    ScriptElement script = doc.createScriptElement();
    script.setSrc("http://connect.facebook.net/en_US/all.js#xfbml=1");
    script.setType("text/javascript");
    script.setLang("javascript");
    doc.getBody().appendChild(script);
  }

  public HTML getHFacebook() {
    return hFacebook;
  }
}