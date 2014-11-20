# So I heard about this problem while browsing the Internet and how it's notorious for stumping like 99%
# of programmers during interviews. I thought I would try my hand at it.

for i in range(1, 101):
    if i % 15 == 0:  # equivalent to i % 3 == 0 and i % 5 == 0
        print "FizzBuzz"
    if i % 3 == 0:
        print "Fizz"
    if i % 5 == 0:
        print "Buzz"
    else:
        print i