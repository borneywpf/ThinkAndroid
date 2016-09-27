package com.think.android.samples.widget;

import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.ScaleXSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.TextAppearanceSpan;
import android.text.style.TypefaceSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.widget.TextView;

import com.think.android.samples.R;
import com.think.android.widget.SpanTextView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by borney on 9/26/16.
 */
public class SpanTextViewDemo extends AppCompatActivity {
    private static final String TAG = "TAGSpanTextViewDemo";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spantextview_demo);

        setTextView();
        setSpanTextView();
        setSpanTextView1();
    }

    private void setTextView() {
        // 创建一个 SpannableString对象
        String msg = "字体测试字体大小一半两倍前景色背景色正常粗体斜体粗斜体下划线删除线x1x2电话邮件网站短信彩信地图X轴综合/bot边框";
        SpannableString msp = new SpannableString(msg);

        // 设置字体(default,default-bold,monospace,serif,sans-serif)
        msp.setSpan(new TypefaceSpan("monospace"), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        msp.setSpan(new TypefaceSpan("serif"), 2, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // 设置字体大小（绝对值,单位：像素）
        msp.setSpan(new AbsoluteSizeSpan(20), 4, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        msp.setSpan(new AbsoluteSizeSpan(20, true), 6, 8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 第二个参数boolean
        // dip，如果为true，表示前面的字体大小单位为dip，否则为像素，同上。

        // 设置字体大小（相对值,单位：像素） 参数表示为默认字体大小的多少倍
        msp.setSpan(new RelativeSizeSpan(0.5f), 8, 10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 0.5f表示默认字体大小的一半
        msp.setSpan(new RelativeSizeSpan(2.0f), 10, 12, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 2.0f表示默认字体大小的两倍

        // 设置字体前景色
        msp.setSpan(new ForegroundColorSpan(Color.MAGENTA), 12, 15, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 设置前景色为洋红色

        // 设置字体背景色
        msp.setSpan(new BackgroundColorSpan(Color.CYAN), 15, 18, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 设置背景色为青色

        // 设置字体样式正常，粗体，斜体，粗斜体
        msp.setSpan(new StyleSpan(android.graphics.Typeface.NORMAL), 18, 20, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 正常
        msp.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 20, 22, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 粗体
        msp.setSpan(new StyleSpan(android.graphics.Typeface.ITALIC), 22, 24, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 斜体
        msp.setSpan(new StyleSpan(android.graphics.Typeface.BOLD_ITALIC), 24, 27,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 粗斜体

        // 设置下划线
        msp.setSpan(new UnderlineSpan(), 27, 30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // 设置删除线
        msp.setSpan(new StrikethroughSpan(), 30, 33, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // 设置上下标
        msp.setSpan(new SubscriptSpan(), 34, 35, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 下标
        msp.setSpan(new SuperscriptSpan(), 36, 37, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 上标

        // 超级链接（需要添加setMovementMethod方法附加响应）
        msp.setSpan(new URLSpan("tel:4155551212"), 37, 39, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 电话
        msp.setSpan(new URLSpan("mailto:webmaster@google.com"), 39, 41, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 邮件
        msp.setSpan(new URLSpan("http://www.baidu.com"), 41, 43, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 网络
        msp.setSpan(new URLSpan("sms:4155551212"), 43, 45, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 短信
        // 使用sms:或者smsto:
        msp.setSpan(new URLSpan("mms:4155551212"), 45, 47, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 彩信
        // 使用mms:或者mmsto:
        msp.setSpan(new URLSpan("geo:38.899533,-77.036476"), 47, 49, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 地图

        // 设置字体大小（相对值,单位：像素） 参数表示为默认字体宽度的多少倍
        msp.setSpan(new ScaleXSpan(2.0f), 49, 51, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 2.0f表示默认字体宽度的两倍，即X轴方向放大为默认字体的两倍，而高度不变

        // 设置字体（依次包括字体名称，字体大小，字体样式，字体颜色，链接颜色）
        ColorStateList csllink = null;
        ColorStateList csl = null;
        XmlResourceParser xppcolor = getResources().getXml(R.color.color);
        try {
            csl = ColorStateList.createFromXml(getResources(), xppcolor);
        } catch (XmlPullParserException e) {
            // TODO: handle exception
            e.printStackTrace();
        } catch (IOException e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        XmlResourceParser xpplinkcolor = getResources().getXml(R.color.linkcolor);
        try {
            csllink = ColorStateList.createFromXml(getResources(), xpplinkcolor);
        } catch (XmlPullParserException e) {
            // TODO: handle exception
            e.printStackTrace();
        } catch (IOException e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        msp.setSpan(new TextAppearanceSpan("monospace", android.graphics.Typeface.BOLD_ITALIC, 50, csl,
                csllink), 51, 53, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // 设置项目符号
//        msp.setSpan(new BulletSpan(android.text.style.BulletSpan.STANDARD_GAP_WIDTH, Color.GREEN), 0,
//                msp.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 第一个参数表示项目符号占用的宽度，第二个参数为项目符号的颜色

        // 设置图片
        Drawable drawable = getResources().getDrawable(R.drawable.ic_launcher);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        msp.setSpan(new ImageSpan(drawable), 53, 57, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // 设置边框
        Drawable bg = getResources().getDrawable(R.drawable.text_background);
        msp.setSpan(new ImageSpan(bg) {
            @Override
            public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y,
                             int bottom, Paint paint) {
                paint.setTypeface(Typeface.create("normal", Typeface.BOLD));
                paint.setTextSize(50);
                int len = Math.round(paint.measureText(text, start, end));
                getDrawable().setBounds(0, 0, len, 60);
                super.draw(canvas, text, start, end, x, top, y, bottom, paint);
                paint.setColor(Color.BLUE);
                paint.setTypeface(Typeface.create("normal", Typeface.BOLD));
                paint.setTextSize(40);
                canvas.drawText(text.subSequence(start, end).toString(), x + 10, y, paint);
            }
        }, 57, 59, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        TextView textView = (TextView) findViewById(R.id.textview);
        textView.setText(msp);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void setSpanTextView() {
        //"字体测试字体大小一半两倍前颜色背景色正常粗体斜体粗斜体下划线删除线X1X2电话邮件网站短信彩信地图拉伸"
        SpanTextView spanTextView = (SpanTextView) findViewById(R.id.spantextview);
        spanTextView.spanedable("字体").typeface("monospace").commit();
        spanTextView.spanedable("测试").typeface("serif").commit();
        spanTextView.spanedable("字体").absoluteSize(20).commit();
        spanTextView.spanedable("大小").absoluteSize(20, true).commit();
        spanTextView.spanedable("一半").relativeSize(0.5f).commit();
        spanTextView.spanedable("两倍").relativeSize(2.0f).commit();
        spanTextView.spanedable("前颜色").color(Color.MAGENTA).commit();
        spanTextView.spanedable("背景色").backgroundColor(Color.CYAN).commit();
        spanTextView.spanedable("正常").type(Typeface.NORMAL).commit();
        spanTextView.spanedable("粗体").type(Typeface.BOLD).commit();
        spanTextView.spanedable("斜体").type(Typeface.ITALIC).commit();
        spanTextView.spanedable("粗斜体").type(Typeface.BOLD_ITALIC).commit();
        spanTextView.spanedable("下划线").underline().commit();
        spanTextView.spanedable("删除线").strikethrough().commit();
        spanTextView.append("X");
        spanTextView.spanedable("1").subscript().commit();
        spanTextView.append("X");
        spanTextView.spanedable("2").superscript().commit();
        spanTextView.spanedable("电话").url("tel:4155551212").commit();
        spanTextView.spanedable("邮件").url("mailto:webmaster@google.com").commit();
        spanTextView.spanedable("网站").url("http://www.baidu.com").commit();
        spanTextView.spanedable("短信").url("sms:4155551212").commit();
        spanTextView.spanedable("彩信").url("mms:4155551212").commit();
        spanTextView.spanedable("地图").url("geo:38.899533,-77.036476").commit();
        spanTextView.spanedable("X轴").scaleX(2.0f).commit();
        // 设置字体（依次包括字体名称，字体大小，字体样式，字体颜色，链接颜色）
        ColorStateList csllink = null;
        ColorStateList csl = null;
        XmlResourceParser xppcolor = getResources().getXml(R.color.color);
        try {
            csl = ColorStateList.createFromXml(getResources(), xppcolor);
        } catch (XmlPullParserException e) {
            // TODO: handle exception
            e.printStackTrace();
        } catch (IOException e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        XmlResourceParser xpplinkcolor = getResources().getXml(R.color.linkcolor);
        try {
            csllink = ColorStateList.createFromXml(getResources(), xpplinkcolor);
        } catch (XmlPullParserException e) {
            // TODO: handle exception
            e.printStackTrace();
        } catch (IOException e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        spanTextView.spanedable("综合").textAppearance("monospace", android.graphics.Typeface.BOLD_ITALIC, 50, csl, csllink).commit();
        spanTextView.spanedable("").bullet(android.text.style.BulletSpan.STANDARD_GAP_WIDTH, Color.GREEN).commit();
        spanTextView.spanedable("/bot").image(getResources().getDrawable(R.drawable.ic_launcher)).commit();
        // 设置边框
        Drawable bg = getResources().getDrawable(R.drawable.text_background);
        spanTextView.spanedable("边框").background(bg).commit();
    }

    private void setSpanTextView1() {
        SpanTextView spanTextView1 = (SpanTextView) findViewById(R.id.spantextview1);
        spanTextView1.append("SpanTextView组合测试[");
        spanTextView1.spanedable("ABCD").color(Color.RED).type(Typeface.ITALIC).absoluteSize(50, true).click(new SpanTextView.OnClickListener() {
            @Override
            public void onClick(CharSequence text) {
                Log.d(TAG, "onClick text = " + text);
            }
        }).commit();
        spanTextView1.append("]组合测试");
    }
}
