# SlidView
仿映客ios端的侧滑效果，也可用于其他的类似的效果
最近在做直播，由于考虑到有些用户只想观看直播，而不需要点赞，群聊等。所以看到了ios端的滑动效果，然后自己写了一个类似的view。
当然他不仅仅用于直播了。

<br/>
##下载
下载工程，将工程中的SlidView.java和SizeUtils.java集成到你的工程中
##使用


      <com.zl.slidview.widget.SlidView xmlns:android="http://schemas.android.com/apk/res/android"
          xmlns:tools="http://schemas.android.com/tools"
          android:layout_width="match_parent"
          android:layout_height="match_parent"
          tools:context="com.zl.slidview.MainActivity">
      
          <LinearLayout
              android:layout_width="match_parent"
              android:layout_height="match_parent"
              android:gravity="center"
              android:background="@android:color/holo_red_light"
              >
              <TextView
                  android:layout_width="wrap_content"
                  android:layout_height="wrap_content"
                  android:text="this is a clidview"
                  android:textColor="#000"
                  android:textSize="30sp"
                  />
          </LinearLayout>
      
      </com.zl.slidview.widget.SlidView>


