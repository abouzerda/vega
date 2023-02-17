import component.Component
import core.GameObject
import core.SAVE_FILE_NAME
import core.Scene
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.BeforeEach
import java.io.File

class GlobalIdSystemTest {
    private val currentScene: Scene = TestScene()

    @Before
    fun init() {
        SAVE_FILE_NAME = "test.json"
    }

    @Test
    fun testGameObjectId() {
        val gameObjects = (1..5).map { Prefabs.generateTestObject() }
        gameObjects.forEach { currentScene.addGameObject(it) }
        currentScene.save()
        currentScene.load()
        val obj = Prefabs.generateTestObject()
        currentScene.addGameObject(obj)
        Assert.assertEquals(gameObjects.maxOf { it.id } + 1, obj.id)
    }

    @Test
    fun testComponentId() {
        val gameObjects = (1..5).map { Prefabs.generateTestObject() }
        gameObjects.forEach {
            currentScene.addGameObject(it)
            it.addComponent<TestComponent>(TestComponent())
        }
        currentScene.save()
        currentScene.load()
        val expected = gameObjects.maxOf { gameObject -> gameObject.components.maxOf { it.id } } + 1
        val component = TestComponent()
        gameObjects.first().addComponent<TestComponent>(component)
        Assert.assertEquals(expected, component.id)
    }

    @BeforeEach
    fun setup() {
        currentScene.gameObjects.clear()
        with(File(SAVE_FILE_NAME)) {
            delete()
        }
        GameObject.ID_COUNTER = 0
        Component.ID_COUNTER = 0
    }

    @After
    fun cleanUp() {
        with(File(SAVE_FILE_NAME)) {
            delete()
        }
    }
}