Below are the runtime analysis of the major functions in my Autocorrect program. 

The levenshteinDistance(one, two) function has a runtime of O(m*k) where m is the length of the first string and k is the length of the second string. This is the runtime because the method uses a dynamic programming approach that fills a 2D array of size (m+1) by (k+1), with each cell computation taking constant time.

