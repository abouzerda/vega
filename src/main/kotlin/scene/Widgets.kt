package scene

import component.SpriteSheet
import imgui.ImGui
import imgui.ImVec2
import org.joml.Vector2f

object Widgets {
    fun showSprites(spriteSheet: SpriteSheet) {
        ImGui.begin("Sprites")
        val windowPos = ImVec2()
        ImGui.getWindowPos(windowPos)
        val windowSize = ImVec2()
        ImGui.getWindowSize(windowSize)
        val itemSpacing = ImVec2()
        ImGui.getStyle().getItemSpacing(itemSpacing)

        val windowX2 = windowPos.x + windowSize.x
        for ((i, sprite) in spriteSheet.sprites.withIndex()) {
            val spriteWidth: Float = (sprite.width * 4).toFloat()
            val spriteHeight: Float = (sprite.height * 4).toFloat()
            val id: Int = sprite.texture!!.texID
            val texCoords: Array<Vector2f> = sprite.texCoords

            ImGui.pushID(i)
            if (ImGui.imageButton(
                    id, spriteWidth, spriteHeight, texCoords[0].x, texCoords[0].y, texCoords[2].x, texCoords[2].y
                )
            ) {
                println("Button " + i + "clicked")
            }
            ImGui.popID()

            val lastButtonPos = ImVec2()
            ImGui.getItemRectMax(lastButtonPos)
            val lastButtonX2 = lastButtonPos.x
            val nextButtonX2 = lastButtonX2 + itemSpacing.x + spriteWidth
            if (i + 1 < spriteSheet.sprites.size && nextButtonX2 < windowX2) {
                ImGui.sameLine()
            }
        }
        ImGui.end()
    }
}