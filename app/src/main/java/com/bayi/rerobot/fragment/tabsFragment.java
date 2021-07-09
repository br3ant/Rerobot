package com.bayi.rerobot.fragment;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bayi.rerobot.App;
import com.bayi.rerobot.R;
import com.bayi.rerobot.adapter.tabAdapter;
import com.bayi.rerobot.gen.SetBeanDao;
import com.bayi.rerobot.greendao.SetBean;
import com.bayi.rerobot.ui.BaseFragment;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

import static com.bayi.rerobot.util.LogUtil.e;
import static com.bayi.rerobot.util.LogUtil.v;
import static com.bayi.rerobot.util.LogUtil.ymdFormat;

/**
 * Created by tongzn
 * on 2020/5/6
 */
public class tabsFragment extends BaseFragment implements OnChartValueSelectedListener {
    public static final String ARG_PAGE = "tzn_PAGE";
    @BindView(R.id.linechart)
    LineChart chart;
    @BindView(R.id.recycle)
    RecyclerView recycle;
    private int mPage = 0;
    private tabAdapter adapter;
    private List<SetBean>setBeans;

    public static tabsFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        tabsFragment pageFragment = new tabsFragment();
        pageFragment.setArguments(args);
        return pageFragment;
    }
    private SimpleDateFormat mFormat = new SimpleDateFormat("HH:mm");
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tabs;
    }
    protected Typeface tfRegular;
    String name="温度表";

    @Override
    protected void initView() {
        tfRegular = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Regular.ttf");
    }

    @Override
    protected void initData() {


    }
   private float max;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Date date = new Date(System.currentTimeMillis());
        String ymd=ymdFormat.format(date);
        mPage = getArguments().getInt(ARG_PAGE);
        switch (mPage){
            case 0:
                max=50f;
                break;
            case 1:
                max=100f;
                break;
            case 2:
                max=100f;
                break;
            case 3:
                max=10f;
                break;
            case 4:
                max=10f;
            case 5:
                max=10f;
                break;

        }
        adapter = new tabAdapter(setBeans,getActivity(),mPage);
        setBeans= App.getDaoSession().getSetBeanDao().queryBuilder().where(SetBeanDao.Properties.Ymd.eq(ymd)).orderDesc(SetBeanDao.Properties.Id).list();
        recycle.setLayoutManager(new LinearLayoutManager(getActivity()));
       // Log.e("mpage",mPage+""+setBeans.size());
        recycle.setAdapter(adapter);
        adapter.setNewData(setBeans);
        {   // // Chart Style // //
            // background color
            chart.setBackgroundColor(Color.WHITE);
            // disable description text
            chart.getDescription().setEnabled(false);
            // enable touch gestures
            chart.setTouchEnabled(true);
            // set listeners
            chart.setOnChartValueSelectedListener(this);
            chart.setDrawGridBackground(false);
            // enable scaling and dragging
            chart.setDragEnabled(true);
            chart.setScaleEnabled(true);
            // chart.setScaleXEnabled(true);
            // chart.setScaleYEnabled(true);

            // force pinch zoom along both axis
            chart.setPinchZoom(true);
        }

        XAxis xAxis;
        {   // // X-Axis Style // //
            xAxis = chart.getXAxis();

            // vertical grid lines
            xAxis.enableGridDashedLine(10f, 10f, 0f);
            xAxis.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    int h=(int)value/60;
                    int m=(int)value%60;
                   return String.format("%s:%s",h,m);
                   //return value+"";
                }
            });
           // xAxis.setSpaceMax(300f);
          //  xAxis.setLabelsToSkip(1);
        }

        YAxis yAxis;
        {   // // Y-Axis Style // //
            yAxis = chart.getAxisLeft();

            // disable dual axis (only use LEFT axis)
            chart.getAxisRight().setEnabled(false);

            // horizontal grid lines
            yAxis.enableGridDashedLine(10f, 10f, 0f);

            // axis range
            yAxis.setAxisMaximum(max);
            yAxis.setAxisMinimum(0f);
        }


        {   // // Create Limit Lines // //
            LimitLine llXAxis = new LimitLine(9f, "Index 10");
            llXAxis.setLineWidth(4f);
            llXAxis.enableDashedLine(10f, 10f, 0f);
            llXAxis.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
            llXAxis.setTextSize(10f);
            llXAxis.setTypeface(tfRegular);

            LimitLine ll1 = new LimitLine(150f, "Upper Limit");
            ll1.setLineWidth(4f);
            ll1.enableDashedLine(10f, 10f, 0f);
            ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
            ll1.setTextSize(10f);
            ll1.setTypeface(tfRegular);

            LimitLine ll2 = new LimitLine(-30f, "Lower Limit");
            ll2.setLineWidth(4f);
            ll2.enableDashedLine(10f, 10f, 0f);
            ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
            ll2.setTextSize(10f);
            ll2.setTypeface(tfRegular);

            // draw limit lines behind data instead of on top
            yAxis.setDrawLimitLinesBehindData(true);
            // add limit lines
            yAxis.addLimitLine(ll1);
            yAxis.addLimitLine(ll2);
            //xAxis.addLimitLine(llXAxis);
        }


        // add data

        setData();

        // draw points over time
        chart.animateX(1500);

        // get the legend (only possible after setting data)
        Legend l = chart.getLegend();

        // draw legend entries as lines
        l.setForm(Legend.LegendForm.LINE);

    }

    public  int getCurrentFloatTime(String currentTime) {
        String[] split = currentTime.split(":");
        return Integer.valueOf(split[0]) * 60 + Integer.valueOf(split[1]) ;
    }

    private void setData( ) {
        ArrayList<Entry> values = new ArrayList<>();
         for(int j=0;j<1440;j++){
        for (int i = 0; i < setBeans.size(); i++) {
              if(getCurrentFloatTime(setBeans.get(i).getHms())==j){
                  switch (mPage) {
                      case 0:
                          values.add(new Entry(j, setBeans.get(i).getTem()));
                          break;
                      case 1:
                          values.add(new Entry(j, setBeans.get(i).getHum()));
                          break;
                      case 2:
                          values.add(new Entry(j, setBeans.get(i).getCo2()));
                          break;
                      case 3:
                          values.add(new Entry(j, setBeans.get(i).getPm25()));
                          break;
                      case 4:
                          values.add(new Entry(j, setBeans.get(i).getVvoc()));
                          break;
                      case 5:
                          values.add(new Entry(j, setBeans.get(i).getPm10()));
                          break;
                  }
              }else
                  continue;

        }
        }

        LineDataSet set1;

        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {

            set1 = (LineDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            set1.notifyDataSetChanged();
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        }else  {
            set1 = new LineDataSet(values, name);
        set1.setDrawIcons(false);
        // draw dashed line
        set1.enableDashedLine(10f, 5f, 0f);
        // black lines and points
        set1.setColor(Color.BLACK);
        set1.setCircleColor(Color.BLACK);

        // line thickness and point size
        set1.setLineWidth(1f);
        set1.setCircleRadius(3f);

        // draw points as solid circles
        set1.setDrawCircleHole(false);

        // customize legend entry
        set1.setFormLineWidth(1f);
        set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
        set1.setFormSize(15.f);

        // text size of values
        set1.setValueTextSize(9f);

        // draw selection line as dashed
        set1.enableDashedHighlightLine(10f, 5f, 0f);

        // set the filled area
        set1.setDrawFilled(true);
        set1.setFillFormatter(new IFillFormatter() {
            @Override
            public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                return chart.getAxisLeft().getAxisMinimum();
            }
        });

        // set color of filled area
        if (Utils.getSDKInt() >= 18) {
            // drawables only supported on api level 18 and above
            Drawable drawable = ContextCompat.getDrawable(getActivity(), R.drawable.fade_red);
            set1.setFillDrawable(drawable);
        } else {
            set1.setFillColor(Color.BLACK);
        }

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1); // add the data sets

        // create a data object with the data sets
        LineData data = new LineData(dataSets);

        // set data
        chart.setData(data);
    }
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
}