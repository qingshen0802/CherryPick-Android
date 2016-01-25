package uk.co.cherrypick.android;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

class ContractFeatureView extends View {


    String mlabel;
    String mvalue;

    public ContractFeatureView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.ContractFeatureView,
                0, 0);

        try {
            mlabel = a.getString(R.styleable.ContractFeatureView_label);
            mvalue = a.getString(R.styleable.ContractFeatureView_value);
        } finally {
            a.recycle();
        }
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View layout = inflater.inflate(R.layout.fragment_contract_feature, container, false);
//
//
//        label = (TextView)layout.findViewById(R.id.labelTextView);
//        value = (TextView)layout.findViewById(R.id.valueTextView);
//
//        label.setText(getArguments().getString("label"));
//        value.setText(getArguments().getString("value"));
//
//
//        return layout;
//    }


//
//    public String getLabel() {
//        return (String)this.label.getText();
//    }
//
//    public void setLabel(String label) {
//        this.label.setText(label);
//    }
//
//    public String getValue() {
//        return (String)this.value.getText();
//    }
//
//    public void setValue(String value) {
//        this.value.setText(value);
//    }
}