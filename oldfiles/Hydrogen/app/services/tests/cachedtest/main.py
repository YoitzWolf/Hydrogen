class CachedStorage(set):
	
	def __init__(self, model):
		self.model = model
		super().__init__(self)

	def add(self, unit):
		if not isinstance(unit, self.model): raise NotImplementedError(f"INVALID TYPE IN CachedStorage.add(unit) need: {self.model}, got: {unit.__class__}")
		super().add(unit)

class Me():
	def __init__(self, unit):
		self.unit = unit

	def __hash__(self):
		return hash(self.unit)

	def __eq__(self, another):
		return self.unit == another.unit
		

cashed_tokens: CachedStorage = CachedStorage(Me)

cashed_tokens.add(Me(1))

print(Me(1) in cashed_tokens)