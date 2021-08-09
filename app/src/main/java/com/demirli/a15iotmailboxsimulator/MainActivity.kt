package com.demirli.a15iotmailboxsimulator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.bottomsheet.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var bsBehavior: BottomSheetBehavior<LinearLayout>

    private var logArrayList = arrayListOf<String>()
    private var logCount: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        open_logPanel_btn.text = "OPEN LOG PANEL (${logCount})"

        val adapter = setAdapter()
        setBottomSheet(adapter)

        open_logPanel_btn.setOnClickListener {
            openLogPanel()
        }

        open_mailbox_btn.setOnClickListener {
            simulateMailBox("Opened")
            adapter.notifyDataSetChanged()
        }

        close_mailbox_btn.setOnClickListener {
            simulateMailBox("Closed")
            adapter.notifyDataSetChanged()
        }

        start_monitoring_btn.setOnClickListener {
            monitoringOpen()
        }

        stop_monitoring_btn.setOnClickListener {
            monitoringClosed()
        }
    }

    fun setAdapter(): ArrayAdapter<String>{
        val adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,logArrayList)
        bottom_sheet_listview.adapter = adapter
        bsBehavior = BottomSheetBehavior.from(bottom_sheet)
        return adapter
    }

    fun setBottomSheet(adapter: ArrayAdapter<String>){

        bsBehavior.addBottomSheetCallback(object: BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                println(newState)
                if(newState == 3){//Opened State
                    open_logPanel_btn.text = "CLOSE LOG PANEL (${logCount})"
                }else if(newState == 4){//Closed State
                    open_logPanel_btn.text = "OPEN LOG PANEL (${logCount})"

                    if(logArrayList.size > 0){
                        var count = 0
                        logArrayList.forEach {
                            logArrayList.set(count,it.replace("(*)",""))
                            count++
                        }
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        })
    }

    fun openLogPanel(){
        logCount = 0
        if (bsBehavior.state != BottomSheetBehavior.STATE_EXPANDED){
            bsBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }else{
            bsBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    fun simulateMailBox(openedOrClosed: String){
        logArrayList.add("Mailbox $openedOrClosed. (*)")
        logCount++
        open_logPanel_btn.text = "OPEN LOG PANEL (${logCount})"
    }

    fun monitoringOpen() {
        mailbox_layout.visibility = View.VISIBLE
        start_monitoring_btn.visibility = View.INVISIBLE
        stop_monitoring_btn.visibility = View.VISIBLE
    }

    fun monitoringClosed() {
        mailbox_layout.visibility = View.INVISIBLE
        start_monitoring_btn.visibility = View.VISIBLE
        stop_monitoring_btn.visibility = View.INVISIBLE
    }

}
