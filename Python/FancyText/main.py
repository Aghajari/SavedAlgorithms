class FancyText:
    height = 5

    def __init__(self, w):
        self.width = w

    def render(self):
        return [[' ' for _ in range(self.width)] for _ in range(self.height)]

    def __repr__(self):
        return '\n'.join([''.join(x) for x in self.render()])

    @staticmethod
    def from_char(char):
        if char == 'A':
            return _A()
        elif char == 'M':
            return _M()
        elif char == 'I':
            return _I()
        elif char == 'R':
            return _R()
        else:
            return FancyText(2)

    @staticmethod
    def generate(string):
        a = ['' for _ in range(FancyText.height)]
        for char in list(string):
            a2 = FancyText.from_char(char).render()
            for j in range(FancyText.height):
                a[j] += ''.join(a2[j])
        return '\n'.join([''.join(x) for x in a])


class _M(FancyText):
    def __init__(self):
        super().__init__(self.height * 2 - 2)

    def render(self):
        a = super().render()
        hw = self.width // 2
        a[1][hw - 1] = '\\'
        a[hw//2][hw - 1] = '\\'
        for i in range(self.height):
            if i > 0: a[i][0] = '|'
            if i > hw//2 - 1: a[i][hw // 2] = '|'

            for j in range(hw):
                if (i == self.height - 1 and 0 < j < hw//2) or (i == 0 and 0 < j < hw - 1):
                    a[i][j] = '_'
                a[i][self.width - j - 1] = '/' if a[i][j] == '\\' else a[i][j]

        return a


class _I(FancyText):
    def __init__(self):
        super().__init__(self.height)

    def render(self):
        a = super().render()
        hw = self.width // 2
        a[0][hw] = '_'
        a[self.height - 1][hw] = '_'
        for i in range(self.height):
            a[i][1 - int(i == 1 or i == self.height - 1)] = '|'

            for j in range(hw):
                if ((i == 0 or i == self.height - 1) and (0 < j < hw)) or (i == j == 1):
                    a[i][j] = '_'
                a[i][self.width - j - 1] = a[i][j]

        return a


class _R(FancyText):
    def __init__(self):
        super().__init__(self.height + 2)

    def render(self):
        a = super().render()
        hw = 3

        for j in range(1, 6):
            a[0][j] = ' ' if j == 5 else '_'
            if j < hw - 1:
                a[self.height - 1][j] = '_'
        a[self.height - 1][self.width - 2] = '_'

        for i in [1, 2, 3]:
            a[i][hw] = '_'

        a[2][hw + 1] = ')'
        a[1][5] = '\\'
        a[3][hw + 2] = '<'
        a[2][6] = '|'

        for i in range(4, self.height):
            a[i][i] = '\\'
            a[i][i + 2] = '\\'

        for i in range(self.height):
            if i > 0:
                a[i][0] = '|'
            if i == 2 or i > 3:
                a[i][2] = '|'

        return a


class _A(FancyText):
    def __init__(self):
        super().__init__(self.height * 2 - 1)

    def render(self):
        a = super().render()
        hw = self.width // 2
        for i, j in [(0, hw), (self.height - 2, hw), (self.height - 3, hw),
                     (self.height - 2, hw - 1), (self.height - 1, hw - 3)]:
            a[i][j] = '_'
        [a[self.height - j - 1].__setitem__(j, '/') for j in range(hw)]
        a[self.height - 1][hw - 2] = '/'

        for i in range(self.height):
            for j in range(hw):
                a[i][self.width - j - 1] = '\\' if a[i][j] == '/' else a[i][j]

        return a


print(FancyText.generate("A M I R"))
