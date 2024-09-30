package com.seven.morningstar.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("/")
public class LandingView extends HorizontalLayout {
    Paragraph banner1 = new Paragraph("MONRINGSTAR");
    Paragraph banner2 = new Paragraph("Reservations made super easy");
    Paragraph banner3 = new Paragraph("Chill, we've got you");
    VerticalLayout bannerLayout = new VerticalLayout();

    Button loginBtn = new Button("Login");
    Button signUpBtn = new Button("SignUp");
    HorizontalLayout btnLayout = new HorizontalLayout();

    public LandingView(){
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);

        configureBanners();
        configureBannerLayout();
        configureBtnLayout();

        add(bannerLayout, btnLayout);
    }

    private void configureBtnLayout() {
        btnLayout.add(signUpBtn, loginBtn);
        btnLayout.addClassNames("btn-layout");
        btnLayout.setWidthFull();
        btnLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        btnLayout.setAlignItems(Alignment.START);
    }

    private void configureBannerLayout() {
        bannerLayout.add(banner1, banner2, banner3);
        bannerLayout.addClassNames("banner-layout");
        bannerLayout.setWidthFull();
        bannerLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        bannerLayout.setAlignItems(Alignment.END);
    }

    private void configureBanners() {
        banner1.addClassName("banner1");
        banner2.addClassName("banner2");
        banner3.addClassName("banner3");
    }
}
