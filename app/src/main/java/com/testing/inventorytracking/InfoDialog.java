package com.testing.inventorytracking;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class InfoDialog extends AppCompatDialogFragment {

    public InfoDialog() {

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder =new AlertDialog.Builder(getActivity());
        builder.setTitle("-Terms & Policies")
                .setMessage("I will follow the guidelines listed below for proper care of the Asset, and I agree on all below terms and policies:\n" +
                        " The Asset is the property of TBTD, The Asset must be at Company during regularly scheduled work days in order to receive administrative communications, upgrades to anti-virus and other software. \n" +
                        "The laptop computer may be taken home or to other locations after company hours by the employee. However, the employee is responsible, at all times, for the care and appropriate use of the Asset. Company Technology Department will have 24/7 access to devices and digital content.\n" +
                        "Each Asset is equipped with security software. It is essential that the employee notify his Department immediately if the laptop is lost or stolen. \n" +
                        "The laptop computer is configured to be used on the school network. The Technology Department will not be able to assist you at your home in order to connect the laptop to other internet providers. \n" +
                        "The Asset is issued to you i3n your current position. If you change positions or Company, the Asset may be reassigned to other Employees.\n" +
                        "Division and Company policies regarding appropriate use, data protection, Asset misuse, health and safety must be adhered to by all users of the Asset. \n" +
                        "\n" +
                        "I will follow the guidelines listed below for proper care of the laptop.\n" +
                        "• I will use the computer for school or professional development purposes. I will not install\n" +
                        "any software on the computer unless it has been approved by the school’s technology\n" +
                        "coordinator. (Requests for software modification or installation should be made 7 days in\n" +
                        "advance of when they are needed.)\n" +
                        "• I will not write on or place any labels or stickers on the laptop.\n" +
                        "• I will not disable or uninstall the virus protection program that is provided with the\n" +
                        "machine.\n" +
                        "• I will report any problems/issues I encounter while using the laptop to the technology\n" +
                        "department immediately through the help desk.\n" +
                        "• I will ensure any documents I create will be moved from the laptop to the network on a\n" +
                        "monthly basis for backup purposes.\n" +
                        "• I understand that the technology staff will reimage the laptop at any point when it\n" +
                        "becomes unusable or unstable and at the end of the year.\n" +
                        "• I understand that reimaging may be a course of action for any repairs or modifications on\n" +
                        "the computer and this will result in the loss of all data from the laptop.\n" +
                        "• Any modifications I make in the computer’s settings will be for usability or cosmetic\n" +
                        "reasons only.\n" +
                        "• All laptops must be returned at the end of the school year for inventory and software\n" +
                        "updates. Laptops will be reassigned as deemed appropriate by the administration.\n" +
                        "Guidelines for Proper Care of the Laptop:\n" +
                        "1. The laptop is not to be loaned to anyone.\n" +
                        "2. Other individuals, including children, should not be allowed to play on the computer.\n" +
                        "3. Proper care is to be given to the laptop at all times, including but not limited to the\n" +
                        "following:\n" +
                        "a) Give care appropriate for any electrical device\n" +"\n" +
                        "b) Use a surge protector or unplug the laptop during electrical storms.\n" + "\n" +
                        "c) Keep food and drink away from the computer.\n" + "\n" +
                        "d) Do not leave the laptop exposed to direct sunlight or extreme cold.\n" + "\n" +
                        "e) Position the laptop on a safe surface so it does not drop or fall.\n" + "\n" +
                        "f) Do not attempt to repair a damaged or malfunctioning laptop.\n" + "\n" +
                        "g) Do not attempt to upgrade the computer or software.\n" + "\n" +
                        "4. Proper security is to be provided for the laptop at all times, including, but not limited to,\n" +
                        "the following:\n" +
                        "a) Secure your laptop in a safe place at the end of the day.\n" + "\n" +
                        "b) Do not leave the laptop in an unlocked car.\n" + "\n" +
                        "c) Do not leave the A/C adapter behind when moving the laptop.\n" )
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismiss();
                    }
                });
        return  builder.create();
    }
}
