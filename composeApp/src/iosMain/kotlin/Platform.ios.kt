
class IOSPlatform: Platform {
    override val name: String = "Mobile"
}

actual fun getPlatform(): Platform = IOSPlatform()