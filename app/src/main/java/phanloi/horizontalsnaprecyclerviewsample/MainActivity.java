package phanloi.horizontalsnaprecyclerviewsample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.recyclerView)
    HorizontalSnapRecyclerView mRecyclerView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.switchSnap)
    Switch mSwitchSnap;
    @BindView(R.id.switchFling)
    Switch mSwitchFling;

    private SimpleAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mAdapter = new SimpleAdapter(getString(R.string.sample), ItemWidth.MATCH_PARENT, getResources().getDimensionPixelOffset(R.dimen.cell_width_medium), getResources().getDimensionPixelOffset(R.dimen.cell_width_small));
        mRecyclerView.setAdapter(mAdapter);

        mSwitchSnap.setChecked(mRecyclerView.isSnap());
        mSwitchFling.setChecked(mRecyclerView.isFling());

        mSwitchSnap.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mRecyclerView.setSnap(isChecked);
            }
        });

        mSwitchFling.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mRecyclerView.setFling(isChecked);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_match_parent) {
            mAdapter.setItemWidth(ItemWidth.MATCH_PARENT);
            return true;
        } else if (id == R.id.action_medium) {
            mAdapter.setItemWidth(ItemWidth.MEDIUM);
            return true;
        } else if (id == R.id.action_small) {
            mAdapter.setItemWidth(ItemWidth.SMALL);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
