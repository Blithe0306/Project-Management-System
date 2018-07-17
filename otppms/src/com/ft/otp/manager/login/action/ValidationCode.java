/**
 *Administrator
 */
package com.ft.otp.manager.login.action;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.patchca.background.SingleColorBackgroundFactory;
import org.patchca.color.ColorFactory;
import org.patchca.color.SingleColorFactory;
import org.patchca.filter.predefined.CurvesRippleFilterFactory;
import org.patchca.filter.predefined.DiffuseRippleFilterFactory;
import org.patchca.filter.predefined.DoubleRippleFilterFactory;
import org.patchca.filter.predefined.MarbleRippleFilterFactory;
import org.patchca.filter.predefined.WobbleRippleFilterFactory;
import org.patchca.font.RandomFontFactory;
import org.patchca.service.ConfigurableCaptchaService;
import org.patchca.text.renderer.BestFitTextRenderer;
import org.patchca.text.renderer.TextRenderer;
import org.patchca.utils.encoder.EncoderHelper;
import org.patchca.word.RandomWordFactory;

/**
 * 生成管理员登录验证的验证码
 *
 * @Date in Feb 10, 2012,11:41:54 AM
 *
 * @version v1.0
 *
 * @author TBM
 */
public class ValidationCode extends HttpServlet {

    private static final long serialVersionUID = -27630021297500589L;
    private ConfigurableCaptchaService captcha = null;
    private ColorFactory color = null;
    private RandomWordFactory word = null;
    private CurvesRippleFilterFactory curves = null;
    private MarbleRippleFilterFactory marble = null;
    private DoubleRippleFilterFactory doubleRipple = null;
    private WobbleRippleFilterFactory wobble = null;
    private DiffuseRippleFilterFactory diffuse = null;
    private RandomFontFactory font = null;
    private TextRenderer textRenderer = null;
    private SingleColorBackgroundFactory backGround = null;

    /**
     * 验证码生成器初始化
     */
    public void init() throws ServletException {
        super.init();
        // 解决linux 各系统登录验证码 显示不出来问题
        System.setProperty("java.awt.headless", "true");

        captcha = new ConfigurableCaptchaService();
        color = new SingleColorFactory(new Color(0, 33, 82));
        word = new RandomWordFactory();
        curves = new CurvesRippleFilterFactory(captcha.getColorFactory());
        doubleRipple = new DoubleRippleFilterFactory();
        wobble = new WobbleRippleFilterFactory();
        diffuse = new DiffuseRippleFilterFactory();
        marble = new MarbleRippleFilterFactory();
        font = new RandomFontFactory();
        textRenderer = new BestFitTextRenderer();
        backGround = new SingleColorBackgroundFactory();

        word.setMaxLength(4);
        word.setMinLength(4);

        curves.setColorFactory(new SingleColorFactory(new Color(0, 33, 82)));
        curves.setStrokeMax(0);

        font.setMaxSize(18);
        font.setMinSize(18);

        textRenderer.setLeftMargin(2);
        textRenderer.setTopMargin(3);
        textRenderer.setRightMargin(4);
        textRenderer.setBottomMargin(4);

        captcha.setWidth(70);
        captcha.setHeight(28);

        backGround.setColorFactory(new SingleColorFactory(new Color(0.0F, 0.0F, 0.0F, 0.0F)));

        captcha.setTextRenderer(textRenderer);
        captcha.setWordFactory(word);
        captcha.setColorFactory(color);
        captcha.setFontFactory(font);
        captcha.setBackgroundFactory(backGround);
    }

    /**
     * 生成验证码
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("image/png");
        response.setHeader("cache", "no-cache");

        HttpSession session = request.getSession(true);
        OutputStream os = response.getOutputStream();
        switch (3) {
            case 0:
                captcha.setFilterFactory(curves);
                break;
            case 1:
                captcha.setFilterFactory(marble);
                break;
            case 2:
                captcha.setFilterFactory(doubleRipple);
                break;
            case 3:
                captcha.setFilterFactory(wobble);
                break;
            case 4:
                captcha.setFilterFactory(diffuse);
                break;
        }
        String codeStr = EncoderHelper.getChallangeAndWriteImage(captcha, "png", os);
        session.setAttribute("validateCode", codeStr);
        os.flush();
        os.close();
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    /* (non-Javadoc)
     * @see javax.servlet.GenericServlet#destroy()
     */
    public void destroy() {
        word = null;
        color = null;
        captcha = null;
        super.destroy();
    }

}
