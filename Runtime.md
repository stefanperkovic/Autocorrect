Below are the runtime analysis of the major functions in my Autocorrect program. 

1. The levenshteinDistance(one, two) function has a runtime of O(m*k) where m is the length of the first string and k is the length of the second string. This is the runtime because the method uses a dynamic programming approach that fills a 2D array of size (m+1) by (k+1), with each cell computation taking constant time.

2. The runTest(typed) function has a runtime of O(nmd + s^2) where n is the number of words in the dictionary, m is the length of the misspelled input word, d is the average length of dictionary words, and s is the number of viable suggestion words. It loops through the entire dictionary, calculating the Levenshtein distance for each word (which takes O(m*d)), and then sorts the viable suggestions using insertion sort. The sorting step takes O(s^2) because it compares and shifts elements based on edit distance and alphabetical order, and s is typically much smaller than n.

3. The main(args) function has a runtime of O(n + t*(nmd + s^2)), where t is the number of user inputs and the rest are the same as above. The dictionary is loaded once which costs O(n) and then it calls runTest for all the user inputs(t times).



