
class AndroidPlatform : Platform {
    override val name: String = "Mobile"
}

actual fun getPlatform(): Platform = AndroidPlatform()