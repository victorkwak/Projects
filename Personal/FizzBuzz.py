# So I heard about this problem while browsing the Internet and how it's notorious for stumping like 99%
# of programmers during interviews. I thought I would try my hand at it. After looking up the specifics,
# I found that FizzBuzz is actually a children's game.
#
# From Wikipedia: Fizz buzz is a group word game for children to teach them about division.[1] Players take
# turns to count incrementally, replacing any number divisible by three with the word "fizz", and any number
# divisible by five with the word "buzz".
#
# The programming problem seems to have added the additional condition of saying "FizzBuzz" if the number
# is divisible by both 3 and 5.

for i in range(1, 101):
    if i % 15 == 0:  # equivalent to i % 3 == 0 and i % 5 == 0
        print "FizzBuzz"
    if i % 3 == 0:
        print "Fizz"
    if i % 5 == 0:
        print "Buzz"
    else:
        print i