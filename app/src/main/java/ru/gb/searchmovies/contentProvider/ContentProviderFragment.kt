package ru.gb.searchmovies.contentProvider

import android.Manifest
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import ru.gb.searchmovies.R
import ru.gb.searchmovies.databinding.FragmentContactsBinding

const val REQUEST_CODE = 707

class ContentProviderFragment:Fragment() {
    private var _binding:FragmentContactsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContactsBinding.inflate(inflater,container,false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermission()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkPermission() {
        context?.let {
            when {
                ContextCompat.checkSelfPermission(it, Manifest.permission.READ_CONTACTS) ==
                        PackageManager.PERMISSION_GRANTED -> {
                    //Доступ к контактам на телефоне есть
                    getContacts()
                }

                //Опционально: если нужно пояснение перед запросом разрешений
                shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS) -> {
                    AlertDialog.Builder(it)
                        .setTitle("Доступ к контактам")
                        .setMessage("Для дальнейшей работы приложения необходим доступ к контактам")
                        .setPositiveButton("Предоставить доступ") { _, _ ->
                            requestPermission()
                        }
                        .setNegativeButton("Отказать") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .create()
                        .show()
                }
                else -> {
                    //Запрашиваем разрешение
                    requestPermission()
                }
            }
        }
    }

    private fun requestPermission() {
        requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), REQUEST_CODE)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE -> {
                // Проверяем, дано ли пользователем разрешение по нашему запросу
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)
                ) {
                    getContacts()
                } else {
                    // Поясните пользователю, что экран останется пустым, потому что доступ к контактам не предоставлен
                    context?.let {
                        AlertDialog.Builder(it)
                            .setTitle("Доступ к контактам")
                            .setMessage("экран останется пустым")
                            .setNegativeButton("Закрыть") { dialog, _ -> dialog.dismiss() }
                            .setPositiveButton("Предоставить доступ") { _, _ ->
                                openSettings()
                            }
                            .create()
                            .show()
                    }
                }
                return
            }
        }
    }

    private fun openSettings() {
        startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).also { intent ->
            intent.data = Uri.fromParts("package", "ru.gb.searchmovies", null)
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getContacts() {
        context?.let { ctx ->
            // Получаем ContentResolver у контекста
            val contentResolver: ContentResolver = ctx.contentResolver
            // Отправляем запрос на получение контактов и получаем ответ в виде Cursor
            val cursorWithContacts: Cursor? = contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                null,
                null,
                //ContactsContract.Contacts.DISPLAY_NAME + " ASC"
            )

            cursorWithContacts?.let { cursor ->
                for (i in 0..cursor.count) {
                    // Переходим на позицию в Cursor
                    if (cursor.moveToPosition(i)) {
                        val namePosition =
                            cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                        // Берём из Cursor столбец с именем
                        val name = if (namePosition >= 0)
                            cursor.getString(namePosition)
                        else "column not found"
                        addViewContact(ctx, name)

                        val hasPhoneColumnPos =
                            cursor.getColumnIndex("has_phone_number")

                        val phoneContact =
                            cursor.getColumnIndex("data1")
                        val phoneNumber = if (phoneContact >= 0)
                            cursor.getString(phoneContact)
                        else "column not found"
                        addViewNumber(ctx, phoneNumber)

                        val hasPhoneNum =
                            if (hasPhoneColumnPos >= 0)
                                if (cursor.getInt(hasPhoneColumnPos) == 1) "есть телефон"
                                else "нет телефона"
                            else "column not found"
                        addViewNumber(ctx, hasPhoneNum)
                    }
                }
            }
            cursorWithContacts?.close()

        }
    }

    private fun addViewNumber(context: Context, textToShow: String) {
        binding.containerForContacts.addView(AppCompatTextView(context).apply {
            text = textToShow
            textSize = resources.getDimension(R.dimen.main_container_number_size)
        })
    }

    private fun addViewContact(context: Context, textToShow: String) {
        binding.containerForContacts.addView(AppCompatTextView(context).apply {
            text = textToShow
            setTextColor(resources.getColor(R.color.colorContactText,null))
            textSize = resources.getDimension(R.dimen.main_container_contact_size)
        })
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
    companion object {
        fun newInstance() = ContentProviderFragment()
    }
}