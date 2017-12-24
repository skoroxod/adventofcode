using System;
using System.Collections.Generic;
using System.IO;
using System.IO.Compression;
using System.Linq;
using System.Runtime.CompilerServices;
using System.Security.Cryptography.X509Certificates;
using System.Text;
using NUnit.Framework;

namespace AdventOfCode2017
{
    public class Rule
    {
        public Rule(string input)
        {
            var ps = input.Split();
            From = ps[0];
            To = ps[2];
        }

        public string From, To;

        public bool Match(char[][] map, int x0, int y0, int size)
        {
            if(GetFrom(map, x0, y0, 1, 1, size) == From) return true;
            if(GetFrom(map, x0, y0+size-1, 1, -1, size) == From) return true;
            if(GetFrom(map, x0+size-1, y0+size-1, -1, -1, size) == From) return true;
            if(GetFrom(map, x0+size-1, y0, -1, 1, size) == From) return true;
            if(GetFrom2(map, x0, y0, 1, 1, size) == From) return true;
            if(GetFrom2(map, x0, y0+size-1, 1, -1, size) == From) return true;
            if(GetFrom2(map, x0+size-1, y0+size-1, -1, -1, size) == From) return true;
            if(GetFrom2(map, x0+size-1, y0, -1, 1, size) == From) return true;
            return false;
        }

        private string GetFrom(char[][] map, int x0, int y0, int dx, int dy, int size)
        {
            var sb = new StringBuilder();
            for (int ix = 0; ix < size; ix++)
            {
                for (int iy = 0; iy < size; iy++)
                    sb.Append(map[y0 + iy * dy][x0 + ix * dx]);
                sb.Append("/");
            }
            return sb.ToString().TrimEnd('/');
        }
        
        private string GetFrom2(char[][] map, int x0, int y0, int dx, int dy, int size)
        {
            var sb = new StringBuilder();
            for (int iy = 0; iy < size; iy++)
            {
                for (int ix = 0; ix < size; ix++)
                    sb.Append(map[y0 + iy * dy][x0 + ix * dx]);
                sb.Append("/");
            }
            return sb.ToString().TrimEnd('/');
        }

        public void ApplyTo(char[][] res, int x0, int y0)
        {
            var rows = To.Split('/');
            for (int y = 0; y < rows.Length; y++)
            for (int x = 0; x < rows.Length; x++)
            {
                res[y0 + y][x0 + x] = rows[y][x];
            }
        }
    }

    [TestFixture]
    public static class Problem
    {
        [Test]
        public static void RunSolve()
        {
            Solve(InputLines);
        }

        public static void Solve(params string[] rules)
        {
            var map = new char[3][]
            {
                ".#.".ToCharArray(),
                "..#".ToCharArray(),
                "###".ToCharArray(),
            };
            var rs = rules.Select(r => new Rule(r)).ToLookup(r => r.From.Length);
            
            var r2 = rs[5].ToArray();
            var r5 = rs[11].ToArray();
            var rulesSets = new[] { r5, r2 };
            Console.WriteLine(map.StrJoin("\n", r =>r.StrJoin("")));
            for (int i = 0; i < 18; i++)
            {
                var patterSize = map.Length % 2 == 0 ? 2 : 3;
                var rulesSet = patterSize == 2 ? r2 : r5;
                map = Simulate(map, rulesSet, patterSize);
                Console.WriteLine(i + " " + map.SelectMany(ps => ps).Count(p => p == '#'));
            }
        }

        private static char[][] Simulate(char[][] map, Rule[] rules, int size)
        {
            var newSize = (map.Length / size)*(size+1);
            var res = newSize.Times(i => new char[newSize]).ToArray();
            var mapSize = map.Length;
            for(int y0 = 0; y0<mapSize; y0+=size)
            for(int x0 = 0; x0<mapSize; x0+=size)
            {
                var rule = rules.Single(r => r.Match(map, x0, y0, size));
                rule.ApplyTo(res, (size+1) * x0 / size, (size+1) * y0 / size);
            }
            return res;
        }

        private static string[] InputLines =>
            File.ReadAllLines(TestContext.CurrentContext.TestDirectory + "\\input.txt");
    }
}