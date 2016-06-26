# simple script to generate test files, currently hardcoded to testformat1
import random
import string

def random_word(width):
    generated=''.join(random.choice(string.lowercase) for i in range(random.randint(5, width)))
    return generated.ljust(width)


def random_integer(lower_bound, upper_bound):
	return random.randint(lower_bound, upper_bound)

def random_boolean_int():
	return str(random.randint(0,1))


def build_row():
	return random_word(10) + random_boolean_int() + str(random_integer(-99,100)).rjust(3)

f = open('testformat1_2015-06-29.txt','w')
for i in range(0, 10000):
	f.write(build_row() + '\n')
f.close() 