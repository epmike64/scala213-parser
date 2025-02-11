class Point(var x: Int, var y: Int) {

	def move(dx: Int, dy: Int): Unit = {
		x = z + dx * dy
	}

	override def sum: Int = A + B * C
}