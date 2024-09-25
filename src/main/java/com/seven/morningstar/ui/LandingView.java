package com.seven.morningstar.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("/")
public class LandingView extends HorizontalLayout {
    private VerticalLayout bannerLayout = new VerticalLayout();
    private Paragraph banner1 = new Paragraph("MORNINGSTAR");
    private Paragraph banner2 = new Paragraph("Reservations made super easy");
    private Paragraph banner3 = new Paragraph("Chill, we've got you");

    private HorizontalLayout btnLayout = new HorizontalLayout();
    private Button signUpBtn = new Button("Sign Up");
    private Button loginBtn = new Button("Login");

    public LandingView(){
    setSizeFull();
    configureBanners();
    configureBannerLayout();
    configureBtnLayout();

    add(bannerLayout, btnLayout);
    setAlignItems(Alignment.CENTER);
    setJustifyContentMode(JustifyContentMode.AROUND);
    setSpacing(false);
    }

    private void configureBanners(){
        banner1.setClassName("banner1");
        banner2.setClassName("banner2");
        banner3.setClassName("banner3");
    }

    private void configureBannerLayout(){
        bannerLayout.addClassName("banner-layout");
        bannerLayout.setWidthFull();
        bannerLayout.add(banner1, banner2, banner3);
        bannerLayout.setAlignItems(Alignment.END);
        bannerLayout.setJustifyContentMode(JustifyContentMode.CENTER);
    }

    private void configureBtnLayout(){
        btnLayout.addClassName("btn-layout");
        btnLayout.setWidthFull();
        btnLayout.add(signUpBtn, loginBtn);
        btnLayout.setAlignItems(Alignment.START);
        btnLayout.setJustifyContentMode(JustifyContentMode.CENTER);
    }
}
