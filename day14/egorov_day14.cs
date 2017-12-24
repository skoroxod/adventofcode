using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using NUnit.Framework;

namespace AdventOfCode2017
{
    [TestFixture]
    public static class Problem
    {
        [Test]
        public static void RunSolve()
        {
            Solve("flqrgnkx");
            Solve("stpzcrnm");
        }

        public static void Solve(string input)
        {
            Console.WriteLine($"input: {input}");
            var map = Enumerable.Range(0, 128)
                .Select(y => KnotHash(input + "-" + y).ToCharArray())
                .ToArray();
            var usedCount = map.Sum(row => row.Count(c => c == '1'));
            Console.WriteLine($"used cells count {usedCount}");

            var regions = 0;
            for (int x = 0; x < 128; x++)
            for (int y = 0; y < 128; y++)
            {
                if (map[x][y] == '1')
                {
                    regions++;
                    Bfs(map, x, y);
                }
            }
            Console.WriteLine($"regions count: {regions}");
            //Console.WriteLine(map.StrJoin("\n", r=>r.StrJoin("")));
            Console.WriteLine();
        }
        
        private static void Bfs(char[][] map, int x, int y)
        {
            if (x < 0 || x > 127 || y < 0 || y > 127 || map[x][y] != '1') return;
            map[x][y] = 'z';
            Bfs(map, x-1, y);
            Bfs(map, x+1, y);
            Bfs(map, x, y-1);
            Bfs(map, x, y+1);
        }

        public static string KnotHash(string s)
        {
            var size = 256;
            var rounds = 64;
            var lens = s.Select(c => (int) c).Concat(new[] { 17, 31, 73, 47, 23 }).ToArray();
            var list = Enumerable.Range(0, size).ToList();
            var cur = 0;
            var skipSize = 0;
            for (var iRound = 0; iRound < rounds; iRound++)
                foreach (var len in lens)
                {
                    var range = list.Skip(cur).Concat(list).Take(len).Reverse().ToList();
                    for (var i = 0; i < len; i++)
                        list[(cur + i) % size] = range[i];
                    cur = (cur + len + skipSize) % size;
                    skipSize++;
                }
            return GetHashAsString(list);
        }
        
        private static string GetHashAsString(List<int> list)
        {
            var bytes = Enumerable.Range(0, 16)
                .Select(start => list.Skip(16 * start).Take(16).Aggregate(0, (a, b) => (byte) a ^ b));
            return string.Join("", bytes.Select(b => Convert.ToString(b, 2).PadLeft(8, '0')));
        }
    }
}