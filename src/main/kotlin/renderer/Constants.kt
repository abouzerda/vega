package renderer

const val MAX_BATCH_SIZE = 1000

const val POS_SIZE = 2
const val COLOR_SIZE = 4
const val TEX_COORDS_SIZE = 2
const val TEX_ID_SIZE = 1

const val POS_OFFSET = 0
const val COLOR_OFFSET = POS_OFFSET + POS_SIZE * java.lang.Float.BYTES
const val TEX_COORDS_OFFSET = COLOR_OFFSET + COLOR_SIZE * java.lang.Float.BYTES
const val TEX_ID_OFFSET = TEX_COORDS_OFFSET + TEX_COORDS_SIZE * java.lang.Float.BYTES
const val VERTEX_SIZE = 9
const val VERTEX_SIZE_BYTES = VERTEX_SIZE * java.lang.Float.BYTES


