using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using NUnit.Framework;
using wondevwoman;

namespace AdventOfCode2017
{
    [TestFixture]
    public static class Problem
    {
        [Test]
        public static void RunSolve()
        {
            Solve("s1,x3/4,pe/b", 5);
            Solve(Input, 16);
        }
        public static void Solve(string danceSpec, int size)
        {
            var s = Enumerable.Range(0, size).Select(i => (char) ('a' + i)).ToArray();
            var init = new string(s);
            var cur = Dance(danceSpec, init);
            Console.WriteLine(cur);
            var period = 1;
            while(init != cur)
            {
                cur = Dance(danceSpec, cur);
                period++;
            }
            var times = 1000000000 % period;
            Console.WriteLine($"period: {period} tail {times}");
            for (int i = 0; i < times; i++)
                cur = Dance(danceSpec, cur);
            Console.WriteLine(cur);
        }

        private static string Dance(string inp, string positions)
        {
            var s = positions.ToCharArray();
            foreach (var cmd in inp.Split(','))
            {
                var sym = cmd[0];
                var ps = cmd.Substring(1).Split('/');
                if (sym == 's') s = Spin(s, int.Parse(ps[0]));
                else if (sym == 'x') Exchange(s, int.Parse(ps[0]), int.Parse(ps[1]));
                else if (sym == 'p') Partner(s, ps[0][0], ps[1][0]);
                else throw new Exception(cmd);
                //Console.WriteLine(new string(s));
            }
            var result = new string(s);
            return result;
        }

        private static void Partner(char[] c, char a, char b)
        {
            Exchange(c, c.IndexOf(a), c.IndexOf(b));
        }

        private static void Exchange(char[] s, int i, int j)
        {
            var t = s[i];
            s[i] = s[j];
            s[j] = t;
        }

        private static char[] Spin(char[] s, int d)
        {
            d = s.Length - d;
            return s.Skip(d).Concat(s.Take(d)).ToArray();
        }

        private static string Input => File.ReadAllText(TestContext.CurrentContext.TestDirectory + "\\input.txt");
    }
}