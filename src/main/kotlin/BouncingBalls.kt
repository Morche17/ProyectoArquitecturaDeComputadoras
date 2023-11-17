import java.awt.*
import java.awt.event.*
import javax.swing.*
import java.util.Random


//Se define la clase BouncingBalls, que extiende JPanel y representa el panel en el que rebotarán las pelotas.
class BouncingBalls : JPanel() {

    //Se declara una lista mutable llamada balls para almacenar las pelotas que se crearán.
    private val balls = mutableListOf<Ball>()

    inner class Animator : Runnable {
        override fun run() {
            while (true) {
                for (ball in balls) {
                    ball.move()
                }
                repaint()
                try {
                    Thread.sleep(50)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }
    }

    //Dentro del constructor init, se establece el color de fondo del panel en blanco.
    init {
        background = Color.WHITE

        /*
        Se agrega un MouseListener al panel para detectar eventos de clic del mouse. Cuando se presiona el mouse,
        se llama a la función createBall con las coordenadas del punto donde se hizo clic.
         */
        addMouseListener(object : MouseAdapter() {
            override fun mousePressed(e: MouseEvent) {
                createBall(e.point)
            }
        })

        /*
        Se crea un hilo (Thread) llamado animator que se encargará de la animación de las pelotas.
        Se instancia un objeto de la clase Animator y se inicia el hilo.
         */
        val animator = Thread(Animator())
        animator.start()
    }

    /*
    La función createBall se utiliza para crear una nueva pelota con una posición inicial y un color aleatorio. Luego,
    se agrega la pelota a la lista balls.
     */
    private fun createBall(initialPosition: Point) {
        val random = Random()
        val color = Color(random.nextInt(256), random.nextInt(256), random.nextInt(256))
        val ball = Ball(initialPosition, color)
        balls.add(ball)
    }

    //Se define una clase interna llamada Ball que representa una pelota. Cada pelota tiene una posición y un color.
    inner class Ball(private var position: Point, private val color: Color) {

        /*
        Se declaran las velocidades de la pelota en las direcciones X e Y.
        Estas velocidades determinan cómo se mueve la pelota en cada iteración del bucle de animación.
        */
        private var speedX = 5
        private var speedY = 5

        /*
        La función move se encarga de actualizar la posición de la pelota en función de sus velocidades.
        Si la pelota alcanza los bordes del panel, cambia de dirección
        al multiplicar la velocidad por -1 en la dirección correspondiente.
         */
        fun move() {
            position.x += speedX
            position.y += speedY

            if (position.x <= 0 || position.x + BALL_SIZE >= width) {
                speedX *= -1
            }
            if (position.y <= 0 || position.y + BALL_SIZE >= height) {
                speedY *= -1
            }
        }

        /*
        La función draw se utiliza para dibujar la pelota en el panel. Configura el color de dibujo con el color
        de la pelota y dibuja un óvalo en la posición actual de la pelota.
         */
        fun draw(g: Graphics) {
            g.color = color
            g.fillOval(position.x, position.y, BALL_SIZE, BALL_SIZE)
        }
    }

    /*
    Esta es una implementación de la función paintComponent que se llama automáticamente cuando se necesita volver
    a dibujar el panel. Dibuja todas las pelotas en su posición actual en el panel.
     */
    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        for (ball in balls) {
            ball.draw(g)
        }
    }

    //La sección companion object define una constante llamada BALL_SIZE que representa el tamaño de las pelotas.
    companion object {
        private const val BALL_SIZE = 30
    }
}

/*
La función main es el punto de entrada del programa. Aquí se crea una ventana (JFrame) que contiene el panel
BouncingBalls. La ventana se configura con un título, un cierre de operación y un tamaño, y se hace
visible para el usuario.
 */
fun main() {
    val frame = JFrame("Bouncing Balls")
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    frame.setSize(800, 600)
    frame.contentPane.add(BouncingBalls())
    frame.isVisible = true
}
