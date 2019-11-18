package it.lares.office20.permission

interface RequestCallback {

    fun onRequestPermissionSuccess()

    fun onRequestPermissionFailure()
}