Stream.of(
Block.makeCuboidShape(0, 0, 0, 16, 2, 16),
Block.makeCuboidShape(0, 14, 0, 16, 16, 16),
Block.makeCuboidShape(14, 2, 0, 16, 14, 2),
Block.makeCuboidShape(0, 2, 0, 2, 14, 2),
Block.makeCuboidShape(0, 2, 2, 16, 14, 16),
Block.makeCuboidShape(2, 2, 1, 14, 14, 2)
).reduce((v1, v2) -> {return VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR);});