# Android Object Serialization/Deserialization example app

## Notes

### Class WriteReadObj

- tried to make this class as Singletone , but keep Android context as static member to use with file operations was a bad thing
- it was easier and better to make it as ordinary class with factory instantiation method and keep context as nonstatic class member
- WriteReadObj constructor instantiate List&lt;Person> member and load it with data from file
- every add, remove operation update List&lt;Person> list and write it to file
- File.exist check correct way (see WriteReadObj.read()):

~~~
new File(context.getFilesDir(),DATA_BIN).exists()
~~~  

### DialogFragment usage

- for parameter passing I use Bundle arguments
- to pass Person object as argument I convert it to CSV format. Other way to implement Parcelable interface.
  For large object a Json library like Genson can be used.

### ListView
ListViews by default don't have a choiceMode set (it's set to none), so the current selection is not indicated visually.
To make it visible I added attributes:
~~~
android:choiceMode="singleChoice"
android:listSelector="@android:color/holo_blue_light"
~~~

In item edit handler to get the selected item in ListView:

~~~
int pos = personsListView.getCheckedItemPosition();
~~~ 

