using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using NUnit.Framework;

namespace AdventOfCode2017
{
    public class Program
    {
        private readonly string[] cmds;

        public int ip;
        public int MulCount;
        public long[] regs = new long[128];

        public Program(string[] cmds)
        {
            this.cmds = cmds;
        }

        private long GetValue(string regOrValue)
        {
            if (int.TryParse(regOrValue, out var value)) return value;
            return regs[regOrValue[0]];
        }

        public bool ExecuteOne()
        {
            if (ip < 0 || ip >= cmds.Length) return false;
            var ps = cmds[ip].Split();
            var cmd = ps[0];
            var x = ps[1];
            if (cmd == "set")
            {
                regs[x[0]] = GetValue(ps[2]);
            }
            else if (cmd == "sub")
            {
                regs[x[0]] = regs[x[0]] - GetValue(ps[2]);
            }
            else if (cmd == "mul")
            {
                MulCount++;
                regs[x[0]] = regs[x[0]] * GetValue(ps[2]);
            }
            else if (cmd == "jnz" && GetValue(x) != 0)
            {
                ip = (int) (ip + GetValue(ps[2]) - 1);
            }
            ip++;
            return true;
        }
    }

    [TestFixture]
    public static class Problem
    {
        public static void Solve(params string[] lines)
        {
            var program = new Program(lines);
            while (program.ExecuteOne())
                //Console.WriteLine(program.ip + " " + program.regs.StrJoin(" "));
                ;
            Console.WriteLine(program.MulCount);
        }

        public static int DecompiledProgram()
        {
            var h = 0;
            for (var b = 109300; b <= 109300 + 17000; b += 17)
            {
                var isPrime = true;
                for (var d = 2; d * d <= b; d++)
                    if (b % d == 0) isPrime = false; // optimized with %!
                if (!isPrime)
                    h++;
            }
            return h;
        }

        private static string[] InputLines =>
            File.ReadAllLines(TestContext.CurrentContext.TestDirectory + "\\input.txt");

        [Test]
        public static void RunSolve()
        {
            //Solve(InputLines);
            Console.WriteLine(DecompiledProgram());
        }
    }
}