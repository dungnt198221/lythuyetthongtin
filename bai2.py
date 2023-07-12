import numpy as np
import matplotlib
from scipy.stats import entropy
import heapq


#Node for using in Huffman coding
class node:
	def __init__(self, freq, symbol, left=None, right=None):
		# frequency of symbol
		self.freq = freq

		# symbol name (character)
		self.symbol = symbol

		# node left of current node
		self.left = left

		# node right of current node
		self.right = right

		# tree direction (0/1)
		self.huff = ''

	def __lt__(self, nxt):
		return self.freq < nxt.freq


#Print Huffman codes
def printNodes(node, val=''):
	# huffman code for current node
	newVal = val + str(node.huff)

	# if node is not an edge node
	# then traverse inside it
	if(node.left):
		printNodes(node.left, newVal)
	if(node.right):
		printNodes(node.right, newVal)

		# if node is edge node then
		# display its huffman code
	if(not node.left and not node.right):
		print(f"{node.symbol} -> {newVal}")
		
#Shannon-Fano coding
def shannon_fano_encoding(symbols):
    # Sort the symbols in descending order based on their probabilities
    symbols = symbols[np.argsort(-symbols['probability'])]
    
    def assign_code(symbols, start, end, code):
        if start > end:
            return

        if start == end:
            # If there is only one symbol in the range, assign the code
            symbols[start]['code'] += code
        else:
            # Calculate the split index
            total_freq = np.sum(symbols['probability'][start:end+1])
            split_index = None
            curr_freq = 0

            for i in range(start, end+1):
                curr_freq += symbols['probability'][i]
                if curr_freq >= total_freq / 2:
                    split_index = i
                    break

            # Assign codes recursively to the left and right subsets
            assign_code(symbols, start, split_index, code + "0")
            assign_code(symbols, split_index+1, end, code + "1")

    
    # Assign codes recursively to the symbols
    assign_code(symbols, 0, len(symbols) - 1, "")
    
    # Return the symbols with assigned codes
    return symbols

#Run the script

#Get the input and captialize it
name = ""
name = (input("Enter your string: "))
name = name.upper()
# Initialize an empty dictionary to store characters and frequencies of the name
char_freq = {}
char_count = 0
# Iterate over each character in the name
for char in name:
    # Skip whitespace characters
    if char.isspace():
        continue
    char_count += 1 
    # Update the frequency of the character
    char_freq[char] = char_freq.get(char, 0) + 1

count_diff_char = len(char_freq)
symbols = np.empty(count_diff_char, dtype=[('symbol', 'U1'), ('probability', float), ('code', 'U10')])

# list containing unused nodes
nodes = []

# converting characters and frequencies
# into huffman tree nodes
i=0
for char, freq in char_freq.items():
    heapq.heappush(nodes, node(freq, char))
    #print(f"{freq} {char}")
    symbols[i] = (char, np.around(freq/char_count, decimals=3), '')
    #print(symbols[i])
    i+=1

encoded_symbols = shannon_fano_encoding(symbols)

while len(nodes) > 1:

    # sort all the nodes in ascending order
    # based on their frequency
    left = heapq.heappop(nodes)
    right = heapq.heappop(nodes)

    # assign directional value to these nodes
    left.huff = 0
    right.huff = 1

    # combine the 2 smallest nodes to create
    # new node as their parent
    newNode = node(left.freq+right.freq, left.symbol+right.symbol, left, right)

    heapq.heappush(nodes, newNode)

printNodes(nodes[0])
print("-------------------------------------------")

# Print the encoded symbols
for symbol in encoded_symbols:
    print(f"Symbol: {symbol['symbol']}, Probability: {symbol['probability']}, Code: {symbol['code']}")
print()

H1 = np.sum(-encoded_symbols['probability'] * np.log2(encoded_symbols['probability']))
H2 = np.sum(encoded_symbols['probability'] * [len(code) for code in encoded_symbols['code']])

Efficiency = H1 / H2
Redundancy = 1 - Efficiency

print(f"Efficiency is: {np.around(Efficiency, decimals=3)}", end = '\n\n')
print(f"Redundancy is: {np.around(Redundancy, decimals=3)}", end = '\n\n')


