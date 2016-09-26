package com.think.android.samples.widget;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.ScaleXSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.TypefaceSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.widget.TextView;

import com.think.android.samples.R;
import com.think.android.widget.SpanTextView;

/**
 * Created by borney on 9/26/16.
 */
public class SpanTextViewDemo extends AppCompatActivity {
    private SpanTextView mSpanTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spantextview_demo);

        setTextView();
        setSpanTextView();
    }

    private void setTextView() {
        SpannableString ss = new SpannableString("字体测试字体大小一半两倍前颜色背景色正常粗体斜体粗斜体下划线删除线X1X2电话邮件网站短信彩信地图拉伸");
        // setSpan会将start到end这间的文本设置成创建的span格式。span可以是图片格式。
        // 设置字体(default,default-bold,monospace,serif,sans-serif)
        ss.setSpan(new TypefaceSpan("monospace"), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 等宽字体
        ss.setSpan(new TypefaceSpan("serif"), 2, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 衬线字体

        // 设置字体绝对大小（绝对值,单位：像素）
        ss.setSpan(new AbsoluteSizeSpan(20), 4, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 第二个参数boolean dip，如果为true，表示前面的字体大小单位为dip，否则为像素，同上。
        ss.setSpan(new AbsoluteSizeSpan(20, true), 6, 8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // 设置字体相对大小（相对值,单位：像素） 参数表示为默认字体大小的多少倍
        ss.setSpan(new RelativeSizeSpan(0.5f), 8, 10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 0.5f表示默认字体大小的一半
        ss.setSpan(new RelativeSizeSpan(2.0f), 10, 12, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 2.0f表示默认字体大小的两倍

        // 设置字体前景色 ，Color.MAGENTA为紫红
        ss.setSpan(new ForegroundColorSpan(Color.MAGENTA), 12, 15, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 设置前景色为洋红色
        // 设置字体背景色 ，Color.CYAN为青绿色
        ss.setSpan(new BackgroundColorSpan(Color.CYAN), 15, 18, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 设置背景色为青色

        // 设置字体样式正常，粗体，斜体，粗斜体
        ss.setSpan(new StyleSpan(android.graphics.Typeface.NORMAL), 18, 20, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 正常
        ss.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 20, 22, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 粗体
        ss.setSpan(new StyleSpan(android.graphics.Typeface.ITALIC), 22, 24, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 斜体
        ss.setSpan(new StyleSpan(android.graphics.Typeface.BOLD_ITALIC), 24, 27, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 粗斜体

        // 设置下划线
        ss.setSpan(new UnderlineSpan(), 27, 30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 设置删除线
        ss.setSpan(new StrikethroughSpan(), 30, 33, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // 设置上下标
        ss.setSpan(new SubscriptSpan(), 34, 35, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 下标
        ss.setSpan(new SuperscriptSpan(), 36, 37, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 上标

        // 超级链接（需要添加setMovementMethod方法附加响应）
        ss.setSpan(new URLSpan("tel:4155551212"), 37, 39, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 电话
        ss.setSpan(new URLSpan("mailto:webmaster@google.com"), 39, 41, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 邮件
        ss.setSpan(new URLSpan("http://www.baidu.com"), 41, 43, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 网络
        ss.setSpan(new URLSpan("sms:4155551212"), 43, 45, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 短信，使用sms:或者smsto:
        ss.setSpan(new URLSpan("mms:4155551212"), 45, 47, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 彩信，使用mms:或者mmsto:
        ss.setSpan(new URLSpan("geo:38.899533,-77.036476"), 47, 49, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 地图

        // 设置字体大小（相对值,单位：像素） 参数表示为默认字体宽度的多少倍
        ss.setSpan(new ScaleXSpan(2.0f), 49, 51, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 2.0f表示默认字体宽度的两倍，即X轴方向放大为默认字体的两倍，而高度不变


        TextView textView = (TextView) findViewById(R.id.textview);
        //textView.setMovementMethod(LinkMovementMethod.getInstance());
        // void android.widget.TextView.setText(CharSequence text)
        textView.setText(ss);
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
        spanTextView.span("X");
        spanTextView.spanedable("2").superscript().commit();
        spanTextView.spanedable("电话").autoLink("tel:4155551212").commit();
        spanTextView.spanedable("邮件").autoLink("mailto:webmaster@google.com").commit();
        spanTextView.spanedable("网站").autoLink("http://www.baidu.com").commit();
        spanTextView.spanedable("短信").autoLink("sms:4155551212").commit();
        spanTextView.spanedable("彩信").autoLink("mms:4155551212").commit();
        spanTextView.spanedable("地图").autoLink("geo:38.899533,-77.036476").commit();
        spanTextView.spanedable("拉伸").scaleX(2.0f).commit();
    }
}
