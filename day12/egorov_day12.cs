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
            Solve(InputLines);
        }

        public static void Solve(string[] edges)
        {
            var graph = edges.Select(
                    e => e.Replace("<->", "").Replace(",", "").Split().Where(s => s != "").ToArray())
                .ToDictionary(p => p[0], p => p.Skip(1).ToArray());

            Console.WriteLine(Bfs(graph, "0", new HashSet<string>()));

            var used = new HashSet<string>();
            var groups = 0;
            foreach (var start in graph.Keys.Where(n => !used.Contains(n)))
            {
                groups++;
                Bfs(graph, start, used);
            }
            Console.WriteLine(groups);
        }

        private static int Bfs(IDictionary<string, string[]> graph, string start, ISet<string> used)
        {
            used.Add(start);
            //Console.WriteLine(">" + start);
            return 1 + graph.GetOrDefault(start, new string[0])
                       .Where(n => !used.Contains(n))
                       .Sum(next => Bfs(graph, next, used));
        }

        private static string input = @"";
        private static string Input => File.ReadAllText(TestContext.CurrentContext.TestDirectory + "\\input.txt");
        private static int[] InputInts => Input.ToInts();

        private static int[][] InputTable => Input.ToIntsTable();

        //static int[] InputLines => File.ReadAllLines(TestContext.CurrentContext.TestDirectory + "\\input.txt").Select(int.Parse).ToArray();
        private static string[] InputLines =>
            File.ReadAllLines(TestContext.CurrentContext.TestDirectory + "\\input.txt");

        private static int[] ToInts(this string text)
        {
            return text.Split(',').Select(int.Parse).ToArray();
        }

        private static int[][] ToIntsTable(this string text)
        {
            return text.ToLines().Select(line => line.Split().Select(int.Parse).ToArray()).ToArray();
        }

        private static string[] ToLines(this string text)
        {
            return text.Split(new[] { "\r", "\n" }, StringSplitOptions.RemoveEmptyEntries);
        }

    }
}